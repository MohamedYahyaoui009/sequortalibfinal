import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { SequortalibfinalSharedModule } from 'app/shared/shared.module';
import { HOME_ROUTE } from './home.route';
import { HomeComponent } from './home.component';

@NgModule({
  imports: [SequortalibfinalSharedModule, RouterModule.forChild([HOME_ROUTE])],
  declarations: [HomeComponent]
})
export class SequortalibfinalHomeModule {}
