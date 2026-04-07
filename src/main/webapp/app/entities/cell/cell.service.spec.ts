import { beforeEach, describe, expect, it } from 'vitest';

import axios from 'axios';
import dayjs from 'dayjs';
import sinon from 'sinon';

import { DATE_FORMAT } from '@/shared/composables/date-format';
import { Cell } from '@/shared/model/cell.model';

import CellService from './cell.service';

const error = {
  response: {
    status: null,
    data: {
      type: null,
    },
  },
};

const axiosStub = {
  get: sinon.stub(axios, 'get'),
  post: sinon.stub(axios, 'post'),
  put: sinon.stub(axios, 'put'),
  patch: sinon.stub(axios, 'patch'),
  delete: sinon.stub(axios, 'delete'),
};

describe('Service Tests', () => {
  describe('Cell Service', () => {
    let service: CellService;
    let elemDefault;
    let currentDate: Date;

    beforeEach(() => {
      service = new CellService();
      currentDate = new Date();
      elemDefault = new Cell(123, 'AAAAAAA', currentDate, 'AAAAAAA', 0, 'AAAAAAA', 'AAAAAAA');
    });

    describe('Service methods', () => {
      it('should find an element', async () => {
        const returnedFromService = { startDate: dayjs(currentDate).format(DATE_FORMAT), ...elemDefault };
        axiosStub.get.resolves({ data: returnedFromService });

        return service.find(123).then(res => {
          expect(res).toMatchObject(elemDefault);
        });
      });

      it('should not find an element', async () => {
        axiosStub.get.rejects(error);
        return service
          .find(123)
          .then()
          .catch(err => {
            expect(err).toMatchObject(error);
          });
      });

      it('should create a Cell', async () => {
        const returnedFromService = { id: 123, startDate: dayjs(currentDate).format(DATE_FORMAT), ...elemDefault };
        const expected = { startDate: currentDate, ...returnedFromService };

        axiosStub.post.resolves({ data: returnedFromService });
        return service.create({}).then(res => {
          expect(res).toMatchObject(expected);
        });
      });

      it('should not create a Cell', async () => {
        axiosStub.post.rejects(error);

        return service
          .create({})
          .then()
          .catch(err => {
            expect(err).toMatchObject(error);
          });
      });

      it('should update a Cell', async () => {
        const returnedFromService = {
          name: 'BBBBBB',
          startDate: dayjs(currentDate).format(DATE_FORMAT),
          description: 'BBBBBB',
          sector: 1,
          lider: 'BBBBBB',
          cordinador: 'BBBBBB',
          ...elemDefault,
        };

        const expected = { startDate: currentDate, ...returnedFromService };
        axiosStub.put.resolves({ data: returnedFromService });

        return service.update(expected).then(res => {
          expect(res).toMatchObject(expected);
        });
      });

      it('should not update a Cell', async () => {
        axiosStub.put.rejects(error);

        return service
          .update({})
          .then()
          .catch(err => {
            expect(err).toMatchObject(error);
          });
      });

      it('should partial update a Cell', async () => {
        const patchObject = {
          name: 'BBBBBB',
          startDate: dayjs(currentDate).format(DATE_FORMAT),
          description: 'BBBBBB',
          sector: 1,
          cordinador: 'BBBBBB',
          ...new Cell(),
        };
        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = { startDate: currentDate, ...returnedFromService };
        axiosStub.patch.resolves({ data: returnedFromService });

        return service.partialUpdate(patchObject).then(res => {
          expect(res).toMatchObject(expected);
        });
      });

      it('should not partial update a Cell', async () => {
        axiosStub.patch.rejects(error);

        return service
          .partialUpdate({})
          .then()
          .catch(err => {
            expect(err).toMatchObject(error);
          });
      });

      it('should return a list of Cell', async () => {
        const returnedFromService = {
          name: 'BBBBBB',
          startDate: dayjs(currentDate).format(DATE_FORMAT),
          description: 'BBBBBB',
          sector: 1,
          lider: 'BBBBBB',
          cordinador: 'BBBBBB',
          ...elemDefault,
        };
        const expected = { startDate: currentDate, ...returnedFromService };
        axiosStub.get.resolves([returnedFromService]);
        return service.retrieve({ sort: {}, page: 0, size: 10 }).then(res => {
          expect(res).toContainEqual(expected);
        });
      });

      it('should not return a list of Cell', async () => {
        axiosStub.get.rejects(error);

        return service
          .retrieve()
          .then()
          .catch(err => {
            expect(err).toMatchObject(error);
          });
      });

      it('should delete a Cell', async () => {
        axiosStub.delete.resolves({ ok: true });
        return service.delete(123).then(res => {
          expect(res.ok).toBeTruthy();
        });
      });

      it('should not delete a Cell', async () => {
        axiosStub.delete.rejects(error);

        return service
          .delete(123)
          .then()
          .catch(err => {
            expect(err).toMatchObject(error);
          });
      });
    });
  });
});
