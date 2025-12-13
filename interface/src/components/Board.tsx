import { Card, CardContent, CardHeader, CardTitle } from '@/components/ui/card';
import { Avatar, AvatarFallback } from '@/components/ui/avatar';
import { Gem } from 'lucide-react';

interface Square {
  id: number;
  treasures: number;
  players: string[];
}

interface Player {
  id: string;
  name: string;
  position: number;
  color: string;
}

interface BoardProps {
  squares: Square[];
  players: Player[];
}

export function Board({ squares, players }: BoardProps) {
  return (
    <Card className="bg-card text-card-foreground">
      <CardHeader>
        <CardTitle className="text-h3 text-foreground">Tablero de Juego</CardTitle>
      </CardHeader>
      <CardContent>
        <div className="relative">
          <img
            src="https://c.animaapp.com/mj4isrsmdKGiJZ/img/ai_1.png"
            alt="underwater ambient background"
            className="w-full h-64 object-cover rounded-lg mb-6 opacity-30"
            loading="lazy"
          />
          
          <div className="grid grid-cols-5 sm:grid-cols-10 gap-2">
            {squares.map((square) => {
              const playersOnSquare = players.filter(p => p.position === square.id);
              
              return (
                <div
                  key={square.id}
                  className="aspect-square bg-muted rounded-lg p-2 flex flex-col items-center justify-center gap-1 border-2 border-border hover:border-primary transition-smooth"
                >
                  <span className="text-label font-medium text-foreground">{square.id}</span>
                  
                  {square.treasures > 0 && (
                    <div className="flex items-center gap-1">
                      <Gem className="w-3 h-3 text-warning" />
                      <span className="text-label text-foreground">{square.treasures}</span>
                    </div>
                  )}
                  
                  {playersOnSquare.length > 0 && (
                    <div className="flex -space-x-2">
                      {playersOnSquare.map((player) => (
                        <Avatar key={player.id} className="w-6 h-6 border-2 border-card">
                          <AvatarFallback 
                            className="text-xs"
                            style={{ 
                              backgroundColor: player.color,
                              color: 'white'
                            }}
                          >
                            {player.name.slice(0, 1).toUpperCase()}
                          </AvatarFallback>
                        </Avatar>
                      ))}
                    </div>
                  )}
                </div>
              );
            })}
          </div>
        </div>
      </CardContent>
    </Card>
  );
}