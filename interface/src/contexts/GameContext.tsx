import { createContext, useContext, useState, ReactNode } from 'react';

interface Player {
  id: string;
  name: string;
  position: number;
  treasures: number;
  score: number;
  color: string;
}

interface Square {
  id: number;
  treasures: number;
  players: string[];
}

interface Game {
  id: string;
  creatorName: string;
  status: 'waiting' | 'in-progress' | 'finished';
  players: Player[];
  maxPlayers: number;
  currentRound: number;
  maxRounds: number;
  oxygen: number;
  maxOxygen: number;
  dice1: number | null;
  dice2: number | null;
  currentPlayer: Player | null;
  isYourTurn: boolean;
  board: Square[];
}

interface GameContextType {
  games: Game[];
  currentGame: Game | null;
  createGame: (playerName: string) => string;
  joinGame: (gameId: string, playerName: string) => void;
  rollDice: (gameId: string) => void;
  pickTreasure: (gameId: string) => void;
  dropTreasure: (gameId: string) => void;
  moveUp: (gameId: string) => void;
  moveDown: (gameId: string) => void;
}

const GameContext = createContext<GameContextType | undefined>(undefined);

const PLAYER_COLORS = ['#1e88e5', '#e53935', '#43a047', '#fb8c00', '#8e24aa', '#5e35b1'];

function generateGameId(): string {
  const chars = 'ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789';
  let result = '';
  for (let i = 0; i < 4; i++) {
    result += chars.charAt(Math.floor(Math.random() * chars.length));
  }
  return result;
}

export function GameProvider({ children }: { children: ReactNode }) {
  const [games, setGames] = useState<Game[]>([
    {
      id: 'AB12',
      creatorName: 'Marina',
      status: 'waiting',
      players: [
        { id: '1', name: 'Marina', position: 0, treasures: 0, score: 0, color: PLAYER_COLORS[0] },
      ],
      maxPlayers: 6,
      currentRound: 1,
      maxRounds: 3,
      oxygen: 25,
      maxOxygen: 25,
      dice1: null,
      dice2: null,
      currentPlayer: null,
      isYourTurn: false,
      board: Array.from({ length: 32 }, (_, i) => ({
        id: i,
        treasures: Math.floor(Math.random() * 4),
        players: [],
      })),
    },
    {
      id: 'XY89',
      creatorName: 'Carlos',
      status: 'in-progress',
      players: [
        { id: '1', name: 'Carlos', position: 5, treasures: 2, score: 0, color: PLAYER_COLORS[0] },
        { id: '2', name: 'Ana', position: 3, treasures: 1, score: 0, color: PLAYER_COLORS[1] },
      ],
      maxPlayers: 6,
      currentRound: 1,
      maxRounds: 3,
      oxygen: 20,
      maxOxygen: 25,
      dice1: 2,
      dice2: 3,
      currentPlayer: { id: '1', name: 'Carlos', position: 5, treasures: 2, score: 0, color: PLAYER_COLORS[0] },
      isYourTurn: false,
      board: Array.from({ length: 32 }, (_, i) => ({
        id: i,
        treasures: Math.floor(Math.random() * 4),
        players: [],
      })),
    },
  ]);

  const [currentGame, setCurrentGame] = useState<Game | null>(null);

  const createGame = (playerName: string): string => {
    const gameId = generateGameId();
    const newGame: Game = {
      id: gameId,
      creatorName: playerName,
      status: 'waiting',
      players: [
        { 
          id: '1', 
          name: playerName, 
          position: 0, 
          treasures: 0, 
          score: 0, 
          color: PLAYER_COLORS[0] 
        },
      ],
      maxPlayers: 6,
      currentRound: 1,
      maxRounds: 3,
      oxygen: 30,
      maxOxygen: 30,
      dice1: null,
      dice2: null,
      currentPlayer: { 
        id: '1', 
        name: playerName, 
        position: 0, 
        treasures: 0, 
        score: 0, 
        color: PLAYER_COLORS[0] 
      },
      isYourTurn: true,
      board: Array.from({ length: 32 }, (_, i) => ({
        id: i,
        treasures: Math.floor(Math.random() * 4),
        players: i === 0 ? ['1'] : [],
      })),
    };

    setGames(prev => [...prev, newGame]);
    setCurrentGame(newGame);
    return newGame.id;
  };

  const joinGame = (gameId: string, playerName: string) => {
    setGames(prev => prev.map(game => {
      if (game.id !== gameId) return game;
      if (game.players.length >= game.maxPlayers) return game;
      if (game.status !== 'waiting') return game;

      const newPlayerId = (game.players.length + 1).toString();
      const newPlayer: Player = {
        id: newPlayerId,
        name: playerName,
        position: 0,
        treasures: 0,
        score: 0,
        color: PLAYER_COLORS[game.players.length % PLAYER_COLORS.length],
      };

      return {
        ...game,
        players: [...game.players, newPlayer],
      };
    }));

    const updatedGame = games.find(g => g.id === gameId);
    if (updatedGame) {
      setCurrentGame(updatedGame);
    }
  };

  const rollDice = (gameId: string) => {
    const dice1 = Math.floor(Math.random() * 6) + 1;
    const dice2 = Math.floor(Math.random() * 6) + 1;
    
    setCurrentGame(prev => {
      if (!prev || prev.id !== gameId) return prev;
      return {
        ...prev,
        dice1,
        dice2,
        oxygen: Math.max(0, prev.oxygen - 1),
      };
    });
  };

  const pickTreasure = (gameId: string) => {
    setCurrentGame(prev => {
      if (!prev || prev.id !== gameId || !prev.currentPlayer) return prev;
      
      const currentSquare = prev.board.find(s => s.id === prev.currentPlayer!.position);
      if (!currentSquare || currentSquare.treasures === 0) return prev;

      return {
        ...prev,
        currentPlayer: {
          ...prev.currentPlayer,
          treasures: prev.currentPlayer.treasures + 1,
        },
        board: prev.board.map(square =>
          square.id === prev.currentPlayer!.position
            ? { ...square, treasures: square.treasures - 1 }
            : square
        ),
      };
    });
  };

  const dropTreasure = (gameId: string) => {
    setCurrentGame(prev => {
      if (!prev || prev.id !== gameId || !prev.currentPlayer) return prev;
      if (prev.currentPlayer.treasures === 0) return prev;

      return {
        ...prev,
        currentPlayer: {
          ...prev.currentPlayer,
          treasures: prev.currentPlayer.treasures - 1,
        },
        board: prev.board.map(square =>
          square.id === prev.currentPlayer!.position
            ? { ...square, treasures: square.treasures + 1 }
            : square
        ),
      };
    });
  };

  const moveUp = (gameId: string) => {
    setCurrentGame(prev => {
      if (!prev || prev.id !== gameId || !prev.currentPlayer) return prev;
      if (prev.currentPlayer.position === 0) return prev;

      return {
        ...prev,
        currentPlayer: {
          ...prev.currentPlayer,
          position: prev.currentPlayer.position - 1,
        },
      };
    });
  };

  const moveDown = (gameId: string) => {
    setCurrentGame(prev => {
      if (!prev || prev.id !== gameId || !prev.currentPlayer) return prev;
      if (prev.currentPlayer.position >= prev.board.length - 1) return prev;

      return {
        ...prev,
        currentPlayer: {
          ...prev.currentPlayer,
          position: prev.currentPlayer.position + 1,
        },
      };
    });
  };

  return (
    <GameContext.Provider
      value={{
        games,
        currentGame,
        createGame,
        joinGame,
        rollDice,
        pickTreasure,
        dropTreasure,
        moveUp,
        moveDown,
      }}
    >
      {children}
    </GameContext.Provider>
  );
}

export function useGame() {
  const context = useContext(GameContext);
  if (context === undefined) {
    throw new Error('useGame must be used within a GameProvider');
  }
  return context;
}