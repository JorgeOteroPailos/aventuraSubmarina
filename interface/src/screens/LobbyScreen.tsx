import { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from '@/components/ui/card';
import { Button } from '@/components/ui/button';
import { Badge } from '@/components/ui/badge';
import { Plus, Users, Eye, Play, LogIn } from 'lucide-react';
import { CreateGameModal } from '../components/CreateGameModal';
import { JoinGameModal } from '../components/JoinGameModal';
import { useGame } from '../contexts/GameContext';

export function LobbyScreen() {
  const [showCreateModal, setShowCreateModal] = useState(false);
  const [showJoinModal, setShowJoinModal] = useState(false);
  const { games } = useGame();
  const navigate = useNavigate();

  const handleJoinGame = (gameId: string) => {
    navigate(`/partida/${gameId}`);
  };

  const getStatusBadge = (status: string) => {
    switch (status) {
      case 'waiting':
        return <Badge className="bg-warning text-warning-foreground">Esperando</Badge>;
      case 'in-progress':
        return <Badge className="bg-success text-success-foreground">En marcha</Badge>;
      case 'finished':
        return <Badge className="bg-gray-500 text-white">Acabada</Badge>;
      default:
        return <Badge className="bg-muted text-muted-foreground">Desconocido</Badge>;
    }
  };

  return (
    <div className="space-y-8">
      <div className="flex flex-col sm:flex-row justify-between items-start sm:items-center gap-4">
        <div>
          <h1 className="text-h1 mb-2">Lobby</h1>
          <p className="text-body text-muted-foreground">
            Únete a una partida existente o crea una nueva aventura submarina
          </p>
        </div>
        <div className="flex gap-3">
          <Button
            onClick={() => setShowJoinModal(true)}
            variant="outline"
            className="bg-transparent text-foreground border-border hover:bg-muted hover:text-foreground"
          >
            <LogIn className="mr-2 w-5 h-5" />
            Unirse
          </Button>
          <Button
            onClick={() => setShowCreateModal(true)}
            className="bg-primary text-primary-foreground hover:bg-primary/90"
          >
            <Plus className="mr-2 w-5 h-5" />
            Crear Partida
          </Button>
        </div>
      </div>

      {games.length === 0 ? (
        <Card className="bg-card text-card-foreground">
          <CardContent className="flex flex-col items-center justify-center py-16 px-4">
            <div className="w-24 h-24 mb-6 rounded-full bg-gradient-1 flex items-center justify-center">
              <Play className="w-12 h-12 text-white" />
            </div>
            <h3 className="text-h3 mb-2 text-center text-foreground">No hay partidas activas</h3>
            <p className="text-body text-muted-foreground text-center mb-6 max-w-md">
              Crea una nueva partida o únete usando un código
            </p>
            <div className="flex gap-3">
              <Button
                onClick={() => setShowJoinModal(true)}
                variant="outline"
                className="bg-transparent text-foreground border-border hover:bg-muted hover:text-foreground"
              >
                <LogIn className="mr-2 w-5 h-5" />
                Unirse con Código
              </Button>
              <Button
                onClick={() => setShowCreateModal(true)}
                className="bg-primary text-primary-foreground hover:bg-primary/90"
              >
                <Plus className="mr-2 w-5 h-5" />
                Crear Partida
              </Button>
            </div>
          </CardContent>
        </Card>
      ) : (
        <div className="grid gap-6 md:grid-cols-2 lg:grid-cols-3">
          {games.map((game) => (
            <Card 
              key={game.id} 
              className="bg-card text-card-foreground hover:shadow-lg transition-smooth cursor-pointer"
              onClick={() => handleJoinGame(game.id)}
            >
              <CardHeader>
                <div className="flex justify-between items-start mb-2">
                  <div>
                    <CardTitle className="text-h3 text-foreground mb-1">
                      Partida {game.id}
                    </CardTitle>
                    <p className="text-label text-muted-foreground">
                      Creada por {game.creatorName}
                    </p>
                  </div>
                  {getStatusBadge(game.status)}
                </div>
                <CardDescription className="text-muted-foreground">
                  <div className="flex items-center gap-2">
                    <Users className="w-4 h-4" />
                    <span>{game.players.length} / {game.maxPlayers} jugadores</span>
                  </div>
                </CardDescription>
              </CardHeader>
              <CardContent>
                <div className="flex gap-2">
                  {game.status === 'waiting' && (
                    <Button
                      onClick={(e) => {
                        e.stopPropagation();
                        handleJoinGame(game.id);
                      }}
                      className="flex-1 bg-primary text-primary-foreground hover:bg-primary/90"
                    >
                      <Play className="mr-2 w-4 h-4" />
                      Unirse
                    </Button>
                  )}
                  {game.status === 'in-progress' && (
                    <Button
                      onClick={(e) => {
                        e.stopPropagation();
                        handleJoinGame(game.id);
                      }}
                      variant="outline"
                      className="flex-1 bg-transparent text-foreground border-border hover:bg-muted hover:text-foreground"
                    >
                      <Eye className="mr-2 w-4 h-4" />
                      Observar
                    </Button>
                  )}
                </div>
              </CardContent>
            </Card>
          ))}
        </div>
      )}

      <CreateGameModal
        open={showCreateModal}
        onClose={() => setShowCreateModal(false)}
      />
      
      <JoinGameModal
        open={showJoinModal}
        onClose={() => setShowJoinModal(false)}
      />
    </div>
  );
}