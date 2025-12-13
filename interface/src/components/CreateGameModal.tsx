import { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import {
  Dialog,
  DialogContent,
  DialogDescription,
  DialogFooter,
  DialogHeader,
  DialogTitle,
} from '@/components/ui/dialog';
import { Button } from '@/components/ui/button';
import { Input } from '@/components/ui/input';
import { Label } from '@/components/ui/label';
import { useGame } from '../contexts/GameContext';
import { useToast } from '../contexts/ToastContext';

interface CreateGameModalProps {
  open: boolean;
  onClose: () => void;
}

export function CreateGameModal({ open, onClose }: CreateGameModalProps) {
  const [playerName, setPlayerName] = useState('');
  const { createGame } = useGame();
  const { addToast } = useToast();
  const navigate = useNavigate();

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    
    if (!playerName.trim()) {
      addToast({
        title: 'Error',
        description: 'Por favor introduce tu nombre',
        variant: 'destructive',
      });
      return;
    }

    const gameId = createGame(playerName);
    
    addToast({
      title: '¡Partida creada!',
      description: `Partida ${gameId} creada. Comparte el código con tus amigos`,
      variant: 'default',
    });
    
    onClose();
    setPlayerName('');
    navigate(`/partida/${gameId}`);
  };

  return (
    <Dialog open={open} onOpenChange={onClose}>
      <DialogContent className="bg-popover text-popover-foreground sm:max-w-md">
        <form onSubmit={handleSubmit}>
          <DialogHeader>
            <DialogTitle className="text-h3 text-foreground">Crear Nueva Partida</DialogTitle>
            <DialogDescription className="text-muted-foreground">
              Introduce tu nombre para comenzar una nueva aventura submarina
            </DialogDescription>
          </DialogHeader>
          
          <div className="space-y-4 py-4">
            <div className="space-y-2">
              <Label htmlFor="player-name" className="text-foreground">Tu nombre</Label>
              <Input
                id="player-name"
                type="text"
                placeholder="Ej: Marina"
                value={playerName}
                onChange={(e) => setPlayerName(e.target.value)}
                className="bg-background text-foreground border-border"
                autoFocus
              />
            </div>
          </div>
          
          <DialogFooter>
            <Button
              type="button"
              variant="outline"
              onClick={onClose}
              className="bg-transparent text-foreground border-border hover:bg-muted hover:text-foreground"
            >
              Cancelar
            </Button>
            <Button 
              type="submit"
              className="bg-primary text-primary-foreground hover:bg-primary/90"
            >
              Iniciar Partida
            </Button>
          </DialogFooter>
        </form>
      </DialogContent>
    </Dialog>
  );
}