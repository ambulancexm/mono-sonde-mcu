import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'iot',
        data: { pageTitle: 'objetConnecteIotApp.iot.home.title' },
        loadChildren: () => import('./iot/iot.module').then(m => m.IotModule),
      },
      {
        path: 'type-iot',
        data: { pageTitle: 'objetConnecteIotApp.typeIot.home.title' },
        loadChildren: () => import('./type-iot/type-iot.module').then(m => m.TypeIotModule),
      },
      {
        path: 'sensor',
        data: { pageTitle: 'objetConnecteIotApp.sensor.home.title' },
        loadChildren: () => import('./sensor/sensor.module').then(m => m.SensorModule),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class EntityRoutingModule {}
