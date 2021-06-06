import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { ITypeIot, TypeIot } from '../type-iot.model';

import { TypeIotService } from './type-iot.service';

describe('Service Tests', () => {
  describe('TypeIot Service', () => {
    let service: TypeIotService;
    let httpMock: HttpTestingController;
    let elemDefault: ITypeIot;
    let expectedResult: ITypeIot | ITypeIot[] | boolean | null;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(TypeIotService);
      httpMock = TestBed.inject(HttpTestingController);

      elemDefault = {
        id: 0,
        name: 'AAAAAAA',
      };
    });

    describe('Service methods', () => {
      it('should find an element', () => {
        const returnedFromService = Object.assign({}, elemDefault);

        service.find(123).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(elemDefault);
      });

      it('should create a TypeIot', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.create(new TypeIot()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a TypeIot', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            name: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a TypeIot', () => {
        const patchObject = Object.assign(
          {
            name: 'BBBBBB',
          },
          new TypeIot()
        );

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign({}, returnedFromService);

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of TypeIot', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            name: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.query().subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush([returnedFromService]);
        httpMock.verify();
        expect(expectedResult).toContainEqual(expected);
      });

      it('should delete a TypeIot', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addTypeIotToCollectionIfMissing', () => {
        it('should add a TypeIot to an empty array', () => {
          const typeIot: ITypeIot = { id: 123 };
          expectedResult = service.addTypeIotToCollectionIfMissing([], typeIot);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(typeIot);
        });

        it('should not add a TypeIot to an array that contains it', () => {
          const typeIot: ITypeIot = { id: 123 };
          const typeIotCollection: ITypeIot[] = [
            {
              ...typeIot,
            },
            { id: 456 },
          ];
          expectedResult = service.addTypeIotToCollectionIfMissing(typeIotCollection, typeIot);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a TypeIot to an array that doesn't contain it", () => {
          const typeIot: ITypeIot = { id: 123 };
          const typeIotCollection: ITypeIot[] = [{ id: 456 }];
          expectedResult = service.addTypeIotToCollectionIfMissing(typeIotCollection, typeIot);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(typeIot);
        });

        it('should add only unique TypeIot to an array', () => {
          const typeIotArray: ITypeIot[] = [{ id: 123 }, { id: 456 }, { id: 70286 }];
          const typeIotCollection: ITypeIot[] = [{ id: 123 }];
          expectedResult = service.addTypeIotToCollectionIfMissing(typeIotCollection, ...typeIotArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const typeIot: ITypeIot = { id: 123 };
          const typeIot2: ITypeIot = { id: 456 };
          expectedResult = service.addTypeIotToCollectionIfMissing([], typeIot, typeIot2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(typeIot);
          expect(expectedResult).toContain(typeIot2);
        });

        it('should accept null and undefined values', () => {
          const typeIot: ITypeIot = { id: 123 };
          expectedResult = service.addTypeIotToCollectionIfMissing([], null, typeIot, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(typeIot);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
