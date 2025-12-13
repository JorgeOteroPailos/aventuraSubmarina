import { createContext, useContext, useState, ReactNode, useEffect } from 'react';

interface User {
  id: string;
  name: string;
}

interface AuthContextType {
  user: User | null;
  login: (username: string, password: string) => boolean;
  register: (username: string, password: string) => void;
  logout: () => void;
  isAuthenticated: boolean;
}

const AuthContext = createContext<AuthContextType | undefined>(undefined);

// Mock user database
const MOCK_USERS = [
  { id: '1', username: 'marina', password: 'marina123', name: 'Marina López' },
  { id: '2', username: 'carlos', password: 'carlos123', name: 'Carlos Ruiz' },
];

export function AuthProvider({ children }: { children: ReactNode }) {
  const [user, setUser] = useState<User | null>(null);
  const [users, setUsers] = useState(MOCK_USERS);

  useEffect(() => {
    const savedUser = localStorage.getItem('currentUser');
    if (savedUser) {
      setUser(JSON.parse(savedUser));
    }
  }, []);

  const login = (username: string, password: string): boolean => {
    const foundUser = users.find(
      u => u.username.toLowerCase() === username.toLowerCase() && u.password === password
    );

    if (foundUser) {
      const loggedUser = {
        id: foundUser.id,
        name: foundUser.name,
      };
      setUser(loggedUser);
      localStorage.setItem('currentUser', JSON.stringify(loggedUser));
      localStorage.setItem('authToken', 'mock-token');
      return true;
    }
    return false;
  };

  const register = (username: string, password: string) => {
    const newUser = {
      id: (users.length + 1).toString(),
      username: username.toLowerCase(),
      password,
      name: username,
    };

    setUsers(prev => [...prev, newUser]);

    const registeredUser = {
      id: newUser.id,
      name: newUser.name,
    };
    setUser(registeredUser);
    localStorage.setItem('currentUser', JSON.stringify(registeredUser));
  };

  const logout = () => {
    setUser(null);
    localStorage.removeItem('currentUser');
  };

  return (
    <AuthContext.Provider value={{ user, login, register, logout, isAuthenticated: !!user }}>
      {children}
    </AuthContext.Provider>
  );
}

export function useAuth() {
  const context = useContext(AuthContext);
  if (context === undefined) {
    throw new Error('useAuth must be used within an AuthProvider');
  }
  return context;
}
