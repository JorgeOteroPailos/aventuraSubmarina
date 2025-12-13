import { useParams } from 'react-router-dom';
import { Card, CardContent, CardHeader, CardTitle } from '@/components/ui/card';
import { Badge } from '@/components/ui/badge';
import { Progress } from '@/components/ui/progress';
import { OxygenMeter } from '../components/OxigenMeter';
import { DiceDisplay } from '../components/DiceDisplay';
import { Board } from '../components/Board';
import { PlayerPanel } from '../components/PlayerPanel';
import { useGame } from '../contexts/GameContext';

export function GameBoardScreen() {
  const { id } = useParams<{ id: string }>();
  const { currentGame } = useGame();

  if (!currentGame || currentGame.id !== id) {
    return (
      <div className="flex items-center justify-center min-h-[60vh]">
        <Card className="bg-card text-card-foreground">
          <CardContent className="p-8">
            <p className="text-body text-muted-foreground">Cargando partida...</p>
          </CardContent>
        </Card>
      </div>
    );
  }

  return (
    <div className="space-y-6">
      <div className="flex flex-col lg:flex-row justify-between items-start lg:items-center gap-4">
        <div>
          <div className="flex items-center gap-3 mb-2">
            <h1 className="text-h1 text-foreground">Partida {currentGame.id}</h1>
            <Badge className="bg-primary text-primary-foreground text-body px-3 py-1">
              {currentGame.id}
            </Badge>
          </div>
          <p className="text-body text-muted-foreground">
            Ronda {currentGame.currentRound} - Turno de {currentGame.currentPlayer?.name}
          </p>
        </div>
      </div>

      {/* Estado Global */}
      <div className="grid gap-4 md:grid-cols-3">
        <Card className="bg-card text-card-foreground">
          <CardHeader>
            <CardTitle className="text-body text-foreground">Oxígeno Restante</CardTitle>
          </CardHeader>
          <CardContent>
            <OxygenMeter current={currentGame.oxygen} max={currentGame.maxOxygen} />
          </CardContent>
        </Card>

        <Card className="bg-card text-card-foreground">
          <CardHeader>
            <CardTitle className="text-body text-foreground">Dados</CardTitle>
          </CardHeader>
          <CardContent>
            <DiceDisplay dice1={currentGame.dice1} dice2={currentGame.dice2} />
          </CardContent>
        </Card>

        <Card className="bg-card text-card-foreground">
          <CardHeader>
            <CardTitle className="text-body text-foreground">Progreso</CardTitle>
          </CardHeader>
          <CardContent className="space-y-2">
            <div className="flex justify-between text-label text-foreground">
              <span>Ronda</span>
              <span className="font-medium">{currentGame.currentRound} / {currentGame.maxRounds}</span>
            </div>
            <Progress 
              value={(currentGame.currentRound / currentGame.maxRounds) * 100} 
              className="h-2"
            />
          </CardContent>
        </Card>
      </div>

      {/* Tablero y Panel de Jugador */}
      <div className="grid gap-6 lg:grid-cols-3">
        <div className="lg:col-span-2">
          <Board 
            squares={currentGame.board} 
            players={currentGame.players}
          />
        </div>

        <div>
          <PlayerPanel 
            player={currentGame.currentPlayer}
            isYourTurn={currentGame.isYourTurn}
            gameId={currentGame.id}
          />
        </div>
      </div>
    </div>
  );
}