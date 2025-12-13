import { useState } from 'react';
import { Card, CardContent, CardHeader, CardTitle } from '@/components/ui/card';
import { Button } from '@/components/ui/button';
import { Input } from '@/components/ui/input';
import { Avatar, AvatarFallback } from '@/components/ui/avatar';
import { Search, Trash2 } from 'lucide-react';
import {
  AlertDialog,
  AlertDialogAction,
  AlertDialogCancel,
  AlertDialogContent,
  AlertDialogDescription,
  AlertDialogFooter,
  AlertDialogHeader,
  AlertDialogTitle,
} from '@/components/ui/alert-dialog';

interface User {
  id: string;
  name: string;
  gamesPlayed: number;
  gamesWon: number;
}

export function UsersScreen() {
  const [searchQuery, setSearchQuery] = useState('');
  const [userToDelete, setUserToDelete] = useState<User | null>(null);
  
  // Mock data
  const [users] = useState<User[]>([
    { id: '1', name: 'Marina López', gamesPlayed: 15, gamesWon: 7 },
    { id: '2', name: 'Carlos Ruiz', gamesPlayed: 23, gamesWon: 12 },
    { id: '3', name: 'Ana García', gamesPlayed: 8, gamesWon: 3 },
    { id: '4', name: 'Pedro Martínez', gamesPlayed: 31, gamesWon: 18 },
  ]);

  const filteredUsers = users.filter(user =>
    user.name.toLowerCase().includes(searchQuery.toLowerCase())
  );

  const handleDeleteUser = () => {
    if (userToDelete) {
      console.log('Deleting user:', userToDelete.id);
      setUserToDelete(null);
    }
  };

  return (
    <div className="space-y-8">
      <div>
        <h1 className="text-h1 mb-2 text-foreground">Usuarios</h1>
        <p className="text-body text-muted-foreground">
          Gestiona los usuarios de la plataforma
        </p>
      </div>

      <Card className="bg-card text-card-foreground">
        <CardHeader>
          <CardTitle className="text-h3 text-foreground">Buscar Usuario</CardTitle>
        </CardHeader>
        <CardContent>
          <div className="relative">
            <Search className="absolute left-3 top-1/2 transform -translate-y-1/2 w-5 h-5 text-muted-foreground" />
            <Input
              type="text"
              placeholder="Buscar por nombre..."
              value={searchQuery}
              onChange={(e) => setSearchQuery(e.target.value)}
              className="pl-10 bg-background text-foreground border-border"
            />
          </div>
        </CardContent>
      </Card>

      <Card className="bg-card text-card-foreground">
        <CardHeader>
          <CardTitle className="text-h3 text-foreground">Lista de Usuarios</CardTitle>
        </CardHeader>
        <CardContent>
          <div className="space-y-4">
            {filteredUsers.map((user) => (
              <div
                key={user.id}
                className="flex items-center gap-4 p-4 rounded-lg bg-muted hover:bg-muted/80 transition-smooth"
              >
                <Avatar className="w-12 h-12">
                  <AvatarFallback className="bg-primary text-primary-foreground">
                    {user.name.slice(0, 2).toUpperCase()}
                  </AvatarFallback>
                </Avatar>

                <div className="flex-1">
                  <p className="font-medium text-foreground">{user.name}</p>
                  <p className="text-label text-muted-foreground">
                    {user.gamesPlayed} partidas jugadas • {user.gamesWon} ganadas
                  </p>
                </div>

                <Button
                  variant="ghost"
                  size="icon"
                  onClick={() => setUserToDelete(user)}
                  className="bg-transparent text-destructive hover:bg-destructive hover:text-destructive-foreground"
                  aria-label={`Eliminar usuario ${user.name}`}
                >
                  <Trash2 className="w-5 h-5" />
                </Button>
              </div>
            ))}

            {filteredUsers.length === 0 && (
              <div className="text-center py-8">
                <p className="text-body text-muted-foreground">
                  No se encontraron usuarios
                </p>
              </div>
            )}
          </div>
        </CardContent>
      </Card>

      <AlertDialog open={!!userToDelete} onOpenChange={() => setUserToDelete(null)}>
        <AlertDialogContent className="bg-popover text-popover-foreground">
          <AlertDialogHeader>
            <AlertDialogTitle className="text-foreground">¿Eliminar usuario?</AlertDialogTitle>
            <AlertDialogDescription className="text-muted-foreground">
              Esta acción no se puede deshacer. Se eliminará permanentemente el usuario{' '}
              <span className="font-medium text-foreground">{userToDelete?.name}</span> y todos sus datos.
            </AlertDialogDescription>
          </AlertDialogHeader>
          <AlertDialogFooter>
            <AlertDialogCancel className="bg-transparent text-foreground border-border hover:bg-muted hover:text-foreground">
              Cancelar
            </AlertDialogCancel>
            <AlertDialogAction
              onClick={handleDeleteUser}
              className="bg-destructive text-destructive-foreground hover:bg-destructive/90"
            >
              Eliminar
            </AlertDialogAction>
          </AlertDialogFooter>
        </AlertDialogContent>
      </AlertDialog>
    </div>
  );
}