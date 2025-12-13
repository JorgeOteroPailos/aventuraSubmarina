import { Card, CardContent, CardDescription, CardHeader, CardTitle } from '@/components/ui/card';
import { Label } from '@/components/ui/label';
import { Switch } from '@/components/ui/switch';
import { Separator } from '@/components/ui/separator';
import { useState } from 'react';

export function SettingsScreen() {
  const [settings, setSettings] = useState({
    soundEffects: true,
    notifications: true,
    animations: true,
    autoSave: true,
  });

  const handleToggle = (key: keyof typeof settings) => {
    setSettings(prev => ({ ...prev, [key]: !prev[key] }));
  };

  return (
    <div className="max-w-3xl mx-auto space-y-8">
      <div>
        <h1 className="text-h1 mb-2 text-foreground">Configuración</h1>
        <p className="text-body text-muted-foreground">
          Personaliza tu experiencia de juego
        </p>
      </div>

      <Card className="bg-card text-card-foreground">
        <CardHeader>
          <CardTitle className="text-h3 text-foreground">Preferencias de Juego</CardTitle>
          <CardDescription className="text-muted-foreground">
            Ajusta cómo quieres jugar
          </CardDescription>
        </CardHeader>
        <CardContent className="space-y-6">
          <div className="flex items-center justify-between">
            <div className="space-y-1">
              <Label htmlFor="sound-effects" className="text-foreground">Efectos de sonido</Label>
              <p className="text-label text-muted-foreground">
                Reproduce sonidos durante el juego
              </p>
            </div>
            <Switch
              id="sound-effects"
              checked={settings.soundEffects}
              onCheckedChange={() => handleToggle('soundEffects')}
            />
          </div>

          <Separator />

          <div className="flex items-center justify-between">
            <div className="space-y-1">
              <Label htmlFor="notifications" className="text-foreground">Notificaciones</Label>
              <p className="text-label text-muted-foreground">
                Recibe alertas cuando sea tu turno
              </p>
            </div>
            <Switch
              id="notifications"
              checked={settings.notifications}
              onCheckedChange={() => handleToggle('notifications')}
            />
          </div>

          <Separator />

          <div className="flex items-center justify-between">
            <div className="space-y-1">
              <Label htmlFor="animations" className="text-foreground">Animaciones</Label>
              <p className="text-label text-muted-foreground">
                Muestra animaciones de dados y movimientos
              </p>
            </div>
            <Switch
              id="animations"
              checked={settings.animations}
              onCheckedChange={() => handleToggle('animations')}
            />
          </div>

          <Separator />

          <div className="flex items-center justify-between">
            <div className="space-y-1">
              <Label htmlFor="auto-save" className="text-foreground">Guardado automático</Label>
              <p className="text-label text-muted-foreground">
                Guarda tu progreso automáticamente
              </p>
            </div>
            <Switch
              id="auto-save"
              checked={settings.autoSave}
              onCheckedChange={() => handleToggle('autoSave')}
            />
          </div>
        </CardContent>
      </Card>
    </div>
  );
}