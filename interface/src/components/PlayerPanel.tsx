import { Avatar, AvatarFallback } from '@/components/ui/avatar';
import { Badge } from '@/components/ui/badge';
import { Dices, Package, PackageMinus, ArrowUp, ArrowDown } from 'lucide-react';
import { useGame } from '../contexts/GameContext';
import { useToast } from '../contexts/ToastContext';

interface Player {
  id: string;
  name: string;
  position: number;
  treasures: number;
  color: string;
}

interface PlayerPanelProps {
  player: Player | null;
  isYourTurn: boolean;
  gameId: string;
}

export function PlayerPanel({ player, isYourTurn, gameId }: PlayerPanelProps) {
  const { rollDice, pickTreasure, dropTreasure, moveUp, moveDown } = useGame();
  const { addToast } = useToast();

  if (!player) {
    return (
      <Card className="bg-card text-card-foreground">
        <CardContent className="p-8 text-center">
          <p className="text-body text-muted-foreground">Esperando jugadores...</p>
        </CardContent>
      </Card>
    );
  }

  const handleAction = (action: () => void, actionName: string) => {
    if (!isYourTurn) {
      addToast({
        title: 'No es tu turno',
        description: 'Espera a que sea tu turno para realizar acciones',
        variant: 'destructive',
      });
      return;
    }
    action();
  };

  return (
    <Card className="bg-card text-card-foreground">
      <CardHeader>
        <div className="flex items-center justify-between">
          <CardTitle className="text-h3 text-foreground">Jugador Actual</CardTitle>
          {isYourTurn ? (
            <Badge className="bg-success text-success-foreground">Tu turno</Badge>
          ) : (
            <Badge className="bg-warning text-warning-foreground">Esperando</Badge>
          )}
        </div>
      </CardHeader>
      <CardContent className="space-y-6">
        <div className="flex items-center gap-4">
          <Avatar className="w-16 h-16">
            <AvatarFallback 
              style={{ 
                backgroundColor: player.color,
                color: 'white'
              }}
            >
              {player.name.slice(0, 2).toUpperCase()}
            </AvatarFallback>
          </Avatar>
          <div>
            <p className="font-medium text-foreground">{player.name}</p>
            <p className="text-label text-muted-foreground">
              Posición: {player.position}
            </p>
            <p className="text-label text-muted-foreground">
              Tesoros: {player.treasures}
            </p>
          </div>
        </div>

        <div className="space-y-3">
          <Button
            onClick={() => handleAction(() => rollDice(gameId), 'Lanzar dados')}
            disabled={!isYourTurn}
            className="w-full bg-primary text-primary-foreground hover:bg-primary/90 disabled:opacity-50"
          >
            <Dices className="mr-2 w-5 h-5" />
            Lanzar Dados
          </Button>

          <Button
            onClick={() => handleAction(() => pickTreasure(gameId), 'Coger tesoro')}
            disabled={!isYourTurn}
            className="w-full bg-secondary text-secondary-foreground hover:bg-secondary/90 disabled:opacity-50"
          >
            <Package className="mr-2 w-5 h-5" />
            Coger Tesoro
          </Button>

          <Button
            onClick={() => handleAction(() => dropTreasure(gameId), 'Dejar tesoro')}
            disabled={!isYourTurn}
            variant="outline"
            className="w-full bg-transparent text-foreground border-border hover:bg-muted hover:text-foreground disabled:opacity-50"
          >
            <PackageMinus className="mr-2 w-5 h-5" />
            Dejar Tesoro
          </Button>

          <div className="grid grid-cols-2 gap-3">
            <Button
              onClick={() => handleAction(() => moveUp(gameId), 'Subir')}
              disabled={!isYourTurn}
              variant="outline"
              className="bg-transparent text-foreground border-border hover:bg-muted hover:text-foreground disabled:opacity-50"
            >
              <ArrowUp className="mr-2 w-5 h-5" />
              Subir
            </Button>

            <Button
              onClick={() => handleAction(() => moveDown(gameId), 'Bajar')}
              disabled={!isYourTurn}
              variant="outline"
              className="bg-transparent text-foreground border-border hover:bg-muted hover:text-foreground disabled:opacity-50"
            >
              <ArrowDown className="mr-2 w-5 h-5" />
              Bajar
            </Button>
          </div>
        </div>

        {!isYourTurn && (
          <div className="p-4 bg-warning/10 rounded-lg border border-warning">
            <p className="text-label text-center text-foreground">
              Esperando tu turno...
            </p>
          </div>
        )}
      </CardContent>
    </Card>
  );
}