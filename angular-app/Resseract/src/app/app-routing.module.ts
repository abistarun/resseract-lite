import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { CanvasComponent } from './canvas/canvas.component';
import { ShowcaseComponent } from './showcase/showcase.component';

const routes: Routes = [
  { path: 'canvas', component: CanvasComponent },
  { path: 'showcase', component: ShowcaseComponent },
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
