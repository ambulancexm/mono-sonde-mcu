import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IIot, Iot } from '../iot.model';

import { IotService } from './iot.service';

describe('Service Tests', () => {
  describe('Iot Service', () => {
    let service: IotService;
    let httpMock: HttpTestingController;
    let elemDefault: IIot;
    let expectedResult: IIot | IIot[] | boolean | null;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(IotService);
      httpMock = TestBed.inject(HttpTestingController);

      elemDefault = {
        id: 0,
        mac: 'AAAAAAA',
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

      it('should create a Iot', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.create(new Iot()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a Iot', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            mac: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a Iot', () => {
        const patchObject = Object.assign({}, new Iot());

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign({}, returnedFromService);

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of Iot', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            mac: 'BBBBBB',
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

      it('should delete a Iot', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addIotToCollectionIfMissing', () => {
        it('should add a Iot to an empty array', () => {
          const iot: IIot = { id: 123 };
          expectedResult = service.addIotToCollectionIfMissing([], iot);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(iot);
        });

        it('should not add a Iot to an array that contains it', () => {
          const iot: IIot = { id: 123 };
          const iotCollection: IIot[] = [
            {
              ...iot,
            },
            { id: 456 },
          ];
          expectedResult = service.addIotToCollectionIfMissing(iotCollection, iot);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a Iot to an array that doesn't contain it", () => {
          const iot: IIot = { id: 123 };
          const iotCollection: IIot[] = [{ id: 456 }];
          expectedResult = service.addIotToCollectionIfMissing(iotCollection, iot);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(iot);
        });

        it('should add only unique Iot to an array', () => {
          const iotArray: IIot[] = [{ id: 123 }, { id: 456 }, { id: 59806 }];
          const iotCollection: IIot[] = [{ id: 123 }];
          expectedResult = service.addIotToCollectionIfMissing(iotCollection, ...iotArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const iot: IIot = { id: 123 };
          const iot2: IIot = { id: 456 };
          expectedResult = service.addIotToCollectionIfMissing([], iot, iot2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(iot);
          expect(expectedResult).toContain(iot2);
        });

        it('should accept null and undefined values', () => {
          const iot: IIot = { id: 123 };
          expectedResult = service.addIotToCollectionIfMissing([], null, iot, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(iot);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
