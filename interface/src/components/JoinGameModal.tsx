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

interface JoinGameModalProps {
  open: boolean;
  onClose: () => void;
}

export function JoinGameModal({ open, onClose }: JoinGameModalProps) {
  const [playerName, setPlayerName] = useState('');
  const [gameId, setGameId] = useState('');
  const { games, joinGame } = useGame();
  const { addToast } = useToast();
  const navigate = useNavigate();

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    
    if (!playerName.trim() || !gameId.trim()) {
      addToast({
        title: 'Error',
        description: 'Por favor completa todos los campos',
        variant: 'destructive',
      });
      return;
    }

    const game = games.find(g => g.id.toUpperCase() === gameId.toUpperCase());
    
    if (!game) {
      addToast({
        title: 'Partida no encontrada',
        description: `No existe ninguna partida con el código ${gameId.toUpperCase()}`,
        variant: 'destructive',
      });
      return;
    }

    if (game.status !== 'waiting') {
      addToast({
        title: 'Partida no disponible',
        description: 'Esta partida ya ha comenzado',
        variant: 'destructive',
      });
      return;
    }

    if (game.players.length >= game.maxPlayers) {
      addToast({
        title: 'Partida llena',
        description: 'Esta partida ya tiene el máximo de jugadores',
        variant: 'destructive',
      });
      return;
    }

    joinGame(game.id, playerName);
    
    addToast({
      title: '¡Te has unido!',
      description: `Te has unido a la partida ${game.id}`,
      variant: 'default',
    });
    
    onClose();
    setPlayerName('');
    setGameId('');
    navigate(`/partida/${game.id}`);
  };

  return (
    <Dialog open={open} onOpenChange={onClose}>
      <DialogContent className="bg-popover text-popover-foreground sm:max-w-md">
        <form onSubmit={handleSubmit}>
          <DialogHeader>
            <DialogTitle className="text-h3 text-foreground">Unirse a Partida</DialogTitle>
            <DialogDescription className="text-muted-foreground">
              Introduce el código de la partida y tu nombre
            </DialogDescription>
          </DialogHeader>
          
          <div className="space-y-4 py-4">
            <div className="space-y-2">
              <Label htmlFor="game-id" className="text-foreground">Código de partida</Label>
              <Input
                id="game-id"
                type="text"
                placeholder="Ej: AB12"
                value={gameId}
                onChange={(e) => setGameId(e.target.value.toUpperCase())}
                maxLength={4}
                className="bg-background text-foreground border-border uppercase text-center text-h3 tracking-widest"
                autoFocus
              />
            </div>
            
            <div className="space-y-2">
              <Label htmlFor="player-name-join" className="text-foreground">Tu nombre</Label>
              <Input
                id="player-name-join"
                type="text"
                placeholder="Ej: Marina"
                value={playerName}
                onChange={(e) => setPlayerName(e.target.value)}
                className="bg-background text-foreground border-border"
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
              Unirse
            </Button>
          </DialogFooter>
        </form>
      </DialogContent>
    </Dialog>
  );
}