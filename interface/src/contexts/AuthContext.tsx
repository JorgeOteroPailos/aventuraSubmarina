import { createContext, useContext, useState, ReactNode, useEffect } from 'react';

interface User {
  username: string;
  roles?: string[];
}

interface AuthContextType {
  user: User | null;
  login: (username: string, password: string) => Promise<boolean>;
  register: (username: string, password: string) => Promise<boolean>;
  logout: () => void;
  isAuthenticated: boolean;
}

const AuthContext = createContext<AuthContextType | undefined>(undefined);

const API_URL = import.meta.env.VITE_API_URL ?? 'http://localhost:8082';

export function AuthProvider({ children }: { children: ReactNode }) {
  const [user, setUser] = useState<User | null>(null);

  // Restaurar sesión
  useEffect(() => {
    const storedUser = localStorage.getItem('user');
    if (storedUser) {
      setUser(JSON.parse(storedUser));
    }
  }, []);

  const login = async (username: string, password: string): Promise<boolean> => {
    console.log('1. Iniciando login...');
    try {
      const res = await fetch(`${API_URL}/autenticacion/login`, {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        credentials: 'include', 
        body: JSON.stringify({ username, password }),
      });
      console.log('2. Response status:', res.status);

      if (!res.ok){
        console.log('3. Response not OK');
        return false;
      }

      const authHeader = res.headers.get('Authorization');
      console.log('4. Authorization header:', authHeader);
      if (!authHeader) return false;

      const token = authHeader.replace('Bearer ', '');

      const loggedUser: User = { username };

      console.log('5. Antes de setUser. user será:', loggedUser);
      setUser(loggedUser);
      console.log('6. Después de setUser');
      
      localStorage.setItem('user', JSON.stringify(loggedUser));
      localStorage.setItem('token', token);

      return true;
    } catch (error) {
      console.error('Login error:', error);
      return false;
    }
  };

  const register = async (username: string, password: string): Promise<boolean> => {
    try {
      const res = await fetch(`${API_URL}/autenticacion/register`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({
          nombre: username,
          contrasena: password,
        }),
      });

      return res.ok;
    } catch (error) {
      console.error('Register error:', error);
      return false;
    }
  };

  const logout = async () => {
    const token = localStorage.getItem('token');

    if (token) {
      await fetch(`${API_URL}/autenticacion/logout`, {
        method: 'POST',
        headers: {
          Authorization: `Bearer ${token}`,
        },
        credentials: 'include',
      });
    }

    setUser(null);
    localStorage.removeItem('user');
    localStorage.removeItem('token');
  };

  return (
    <AuthContext.Provider
      value={{
        user,
        login,
        register,
        logout,
        isAuthenticated: !!user,
      }}
    >
      {children}
    </AuthContext.Provider>
  );
}

export function useAuth() {
  const context = useContext(AuthContext);
  if (!context) {
    throw new Error('useAuth must be used within an AuthProvider');
  }
  return context;
}
