import { useParams, useNavigate } from 'react-router-dom';
import { Card, CardContent, CardHeader, CardTitle } from '@/components/ui/card';
import { Button } from '@/components/ui/button';
import { Avatar, AvatarFallback } from '@/components/ui/avatar';
import { Trophy, Medal, Home } from 'lucide-react';
import { useGame } from '../contexts/GameContext';

export function GameFinishedScreen() {
  const { id } = useParams<{ id: string }>();
  const navigate = useNavigate();
  const { currentGame } = useGame();

  if (!currentGame || currentGame.id !== id) {
    return (
      <div className="flex items-center justify-center min-h-[60vh]">
        <Card className="bg-card text-card-foreground">
          <CardContent className="p-8">
            <p className="text-body text-muted-foreground">Partida no encontrada</p>
          </CardContent>
        </Card>
      </div>
    );
  }

  const sortedPlayers = [...currentGame.players].sort((a, b) => b.score - a.score);
  const winner = sortedPlayers[0];

  return (
    <div className="max-w-4xl mx-auto space-y-8">
      <div className="text-center">
        <div className="inline-flex items-center justify-center w-24 h-24 rounded-full bg-gradient-1 mb-6">
          <Trophy className="w-12 h-12 text-white" />
        </div>
        <h1 className="text-h1 mb-2 text-foreground">¡Partida Finalizada!</h1>
        <p className="text-h3 text-muted-foreground">
          {winner.name} ha ganado la aventura submarina
        </p>
      </div>

      <Card className="bg-card text-card-foreground">
        <CardHeader>
          <CardTitle className="text-h2 text-center text-foreground">Ranking Final</CardTitle>
        </CardHeader>
        <CardContent>
          <div className="space-y-4">
            {sortedPlayers.map((player, index) => (
              <div
                key={player.id}
                className={`
                  flex items-center gap-4 p-4 rounded-lg transition-smooth
                  ${index === 0 ? 'bg-gradient-1' : 'bg-muted'}
                `}
              >
                <div className="flex items-center justify-center w-12 h-12">
                  {index === 0 && <Trophy className="w-8 h-8 text-white" />}
                  {index === 1 && <Medal className="w-8 h-8 text-gray-400" />}
                  {index === 2 && <Medal className="w-8 h-8 text-gray-600" />}
                  {index > 2 && (
                    <span className={`text-h3 font-medium ${index === 0 ? 'text-white' : 'text-foreground'}`}>
                      {index + 1}
                    </span>
                  )}
                </div>

                <Avatar className="w-12 h-12">
                  <AvatarFallback 
                    className={index === 0 ? 'bg-white text-primary' : 'bg-primary text-primary-foreground'}
                  >
                    {player.name.slice(0, 2).toUpperCase()}
                  </AvatarFallback>
                </Avatar>

                <div className="flex-1">
                  <p className={`font-medium ${index === 0 ? 'text-white' : 'text-foreground'}`}>
                    {player.name}
                  </p>
                  <p className={`text-label ${index === 0 ? 'text-white/80' : 'text-muted-foreground'}`}>
                    {player.treasures} tesoros recogidos
                  </p>
                </div>

                <div className="text-right">
                  <p className={`text-h2 font-medium ${index === 0 ? 'text-white' : 'text-foreground'}`}>
                    {player.score}
                  </p>
                  <p className={`text-label ${index === 0 ? 'text-white/80' : 'text-muted-foreground'}`}>
                    puntos
                  </p>
                </div>
              </div>
            ))}
          </div>
        </CardContent>
      </Card>

      <div className="flex justify-center">
        <Button
          onClick={() => navigate('/lobby')}
          className="bg-primary text-primary-foreground hover:bg-primary/90"
        >
          <Home className="mr-2 w-5 h-5" />
          Volver al Lobby
        </Button>
      </div>
    </div>
  );
}