import { BrowserRouter as Router, Routes, Route, Navigate } from 'react-router-dom';
import { AppShell } from './components/AppShell';
import { LoginScreen } from './screens/LoginScreen';
import { LobbyScreen } from './screens/LobbyScreen';
import { GameBoardScreen } from './screens/GameBoardScreen';
import { GameFinishedScreen } from './screens/GameFinishedScreen';
import { UsersScreen } from './screens/UsersScreen';
import { SettingsScreen } from './screens/SettingsScreen';
import { AuthProvider, useAuth } from './contexts/AuthContext';
import { GameProvider } from './contexts/GameContext';
import { ToastProvider } from './contexts/ToastContext';

function ProtectedRoute({ children }: { children: React.ReactNode }) {
  const { isAuthenticated } = useAuth();
  return isAuthenticated ? <>{children}</> : <Navigate to="/login" replace />;
}

function AppRoutes() {
  const { isAuthenticated } = useAuth();

  return (
    <Routes>
      <Route path="/login" element={isAuthenticated ? <Navigate to="/lobby" replace /> : <LoginScreen />} />
      <Route path="/" element={<Navigate to={isAuthenticated ? "/lobby" : "/login"} replace />} />
      <Route
        path="/lobby"
        element={
          <ProtectedRoute>
            <AppShell>
              <LobbyScreen />
            </AppShell>
          </ProtectedRoute>
        }
      />
      <Route
        path="/partida/:id"
        element={
          <ProtectedRoute>
            <AppShell>
              <GameBoardScreen />
            </AppShell>
          </ProtectedRoute>
        }
      />
      <Route
        path="/partida/:id/finished"
        element={
          <ProtectedRoute>
            <AppShell>
              <GameFinishedScreen />
            </AppShell>
          </ProtectedRoute>
        }
      />
      <Route
        path="/usuarios"
        element={
          <ProtectedRoute>
            <AppShell>
              <UsersScreen />
            </AppShell>
          </ProtectedRoute>
        }
      />
      <Route
        path="/configuracion"
        element={
          <ProtectedRoute>
            <AppShell>
              <SettingsScreen />
            </AppShell>
          </ProtectedRoute>
        }
      />
    </Routes>
  );
}

function App() {
  return (
    <AuthProvider>
      <GameProvider>
        <ToastProvider>
          <Router>
            <AppRoutes />
          </Router>
        </ToastProvider>
      </GameProvider>
    </AuthProvider>
  );
}

export default App;
