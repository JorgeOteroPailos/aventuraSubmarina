import { Link, useLocation } from 'react-router-dom';
import { Home, Gamepad2, Users, Settings, LogOut, Menu, X } from 'lucide-react';
import { Button } from '@/components/ui/button';
import { Separator } from '@/components/ui/separator';
import { useAuth } from '../contexts/AuthContext';
import { useState, useEffect } from 'react';

interface SidebarNavProps {
  collapsed: boolean;
  onToggle: () => void;
}

export function SidebarNav({ collapsed, onToggle }: SidebarNavProps) {
  const location = useLocation();
  const { logout } = useAuth();
  const [mobileOpen, setMobileOpen] = useState(false);

  useEffect(() => {
    setMobileOpen(false);
  }, [location.pathname]);

  const navItems = [
    { path: '/lobby', label: 'Lobby', icon: Home },
    { path: '/partida', label: 'Partida', icon: Gamepad2, disabled: !location.pathname.includes('/partida') },
    { path: '/usuarios', label: 'Usuarios', icon: Users },
    { path: '/configuracion', label: 'Configuración', icon: Settings },
  ];

  const isActive = (path: string) => {
    if (path === '/partida') {
      return location.pathname.includes('/partida');
    }
    return location.pathname === path;
  };

  const handleLogout = () => {
    logout();
  };

  const sidebarContent = (
    <>
      <div className="p-4 md:p-6">
        <div className="flex items-center justify-between mb-8">
          {!collapsed && (
            <h2 className="text-h3 font-medium bg-gradient-1 bg-clip-text text-transparent">
              Aventura Submarina
            </h2>
          )}
          <Button
            variant="ghost"
            size="icon"
            onClick={onToggle}
            className="hidden md:flex bg-transparent text-foreground hover:bg-muted hover:text-foreground"
            aria-label={collapsed ? 'Expandir menú' : 'Colapsar menú'}
          >
            {collapsed ? <Menu className="w-5 h-5" /> : <X className="w-5 h-5" />}
          </Button>
        </div>

        <nav className="space-y-2" role="navigation" aria-label="Navegación principal">
          {navItems.map((item) => {
            const Icon = item.icon;
            const active = isActive(item.path);
            
            return (
              <Link
                key={item.path}
                to={item.disabled ? '#' : item.path}
                className={`
                  flex items-center gap-3 px-4 py-3 rounded-lg transition-smooth
                  ${active 
                    ? 'bg-primary text-primary-foreground' 
                    : 'text-foreground hover:bg-muted hover:text-foreground'
                  }
                  ${item.disabled ? 'opacity-50 cursor-not-allowed' : 'cursor-pointer'}
                  ${collapsed ? 'justify-center' : ''}
                `}
                aria-current={active ? 'page' : undefined}
                aria-disabled={item.disabled}
              >
                <Icon className="w-5 h-5 flex-shrink-0" />
                {!collapsed && <span className="font-normal">{item.label}</span>}
              </Link>
            );
          })}
        </nav>
      </div>

      <div className="mt-auto p-4 md:p-6">
        <Separator className="mb-4" />
        <Button
          variant="ghost"
          onClick={handleLogout}
          className={`
            w-full bg-transparent text-foreground hover:bg-destructive hover:text-destructive-foreground
            ${collapsed ? 'justify-center' : 'justify-start'}
          `}
        >
          <LogOut className="w-5 h-5 flex-shrink-0" />
          {!collapsed && <span className="ml-3 font-normal">Salir</span>}
        </Button>
      </div>
    </>
  );

  return (
    <>
      {/* Mobile Menu Button */}
      <Button
        variant="ghost"
        size="icon"
        onClick={() => setMobileOpen(!mobileOpen)}
        className="fixed top-4 left-4 z-50 md:hidden bg-card text-foreground hover:bg-muted hover:text-foreground"
        aria-label="Abrir menú"
      >
        <Menu className="w-6 h-6" />
      </Button>

      {/* Mobile Sidebar */}
      {mobileOpen && (
        <div 
          className="fixed inset-0 bg-gray-900/50 z-40 md:hidden"
          onClick={() => setMobileOpen(false)}
        >
          <aside
            className="fixed left-0 top-0 bottom-0 w-64 bg-card shadow-lg z-50 flex flex-col transition-smooth"
            onClick={(e) => e.stopPropagation()}
          >
            <div className="flex justify-end p-4">
              <Button
                variant="ghost"
                size="icon"
                onClick={() => setMobileOpen(false)}
                className="bg-transparent text-foreground hover:bg-muted hover:text-foreground"
                aria-label="Cerrar menú"
              >
                <X className="w-6 h-6" />
              </Button>
            </div>
            {sidebarContent}
          </aside>
        </div>
      )}

      {/* Desktop Sidebar */}
      <aside
        className={`
          hidden md:flex flex-col bg-card border-r border-border transition-smooth
          ${collapsed ? 'w-20' : 'w-64'}
        `}
      >
        {sidebarContent}
      </aside>
    </>
  );
}