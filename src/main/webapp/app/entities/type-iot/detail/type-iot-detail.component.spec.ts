import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { TypeIotDetailComponent } from './type-iot-detail.component';

describe('Component Tests', () => {
  describe('TypeIot Management Detail Component', () => {
    let comp: TypeIotDetailComponent;
    let fixture: ComponentFixture<TypeIotDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [TypeIotDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ typeIot: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(TypeIotDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(TypeIotDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load typeIot on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.typeIot).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
