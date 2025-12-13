import { Progress } from '@/components/ui/progress';
import { Droplet } from 'lucide-react';

interface OxygenMeterProps {
  current: number;
  max: number;
}

export function OxygenMeter({ current, max }: OxygenMeterProps) {
  const percentage = (current / max) * 100;
  
  const getColor = () => {
    if (percentage > 60) return 'text-success';
    if (percentage > 30) return 'text-warning';
    return 'text-destructive';
  };

  return (
    <div className="space-y-3">
      <div className="flex items-center justify-between">
        <div className="flex items-center gap-2">
          <Droplet className={`w-5 h-5 ${getColor()}`} />
          <span className="text-body font-medium text-foreground">
            {current} / {max}
          </span>
        </div>
        <span className={`text-label font-medium ${getColor()}`}>
          {percentage.toFixed(0)}%
        </span>
      </div>
      <Progress value={percentage} className="h-3" />
    </div>
  );
}