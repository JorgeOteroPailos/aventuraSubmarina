import { Dices } from 'lucide-react';

interface DiceDisplayProps {
  dice1: number | null;
  dice2: number | null;
  isRolling?: boolean;
}

export function DiceDisplay({ dice1, dice2, isRolling = false }: DiceDisplayProps) {
  return (
    <div className="flex items-center justify-center gap-4">
      <div
        className={`
          w-16 h-16 rounded-lg bg-gradient-1 flex items-center justify-center
          ${isRolling ? 'animate-dice-roll' : ''}
        `}
      >
        {dice1 !== null ? (
          <span className="text-h2 font-medium text-white">{dice1}</span>
        ) : (
          <Dices className="w-8 h-8 text-white" />
        )}
      </div>
      
      <div
        className={`
          w-16 h-16 rounded-lg bg-gradient-2 flex items-center justify-center
          ${isRolling ? 'animate-dice-roll' : ''}
        `}
      >
        {dice2 !== null ? (
          <span className="text-h2 font-medium text-white">{dice2}</span>
        ) : (
          <Dices className="w-8 h-8 text-white" />
        )}
      </div>
    </div>
  );
}