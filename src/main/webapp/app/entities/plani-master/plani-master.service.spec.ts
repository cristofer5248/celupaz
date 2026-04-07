import { beforeEach, describe, expect, it } from 'vitest';

import axios from 'axios';
import dayjs from 'dayjs';
import sinon from 'sinon';

import { DATE_FORMAT } from '@/shared/composables/date-format';
import { PlaniMaster } from '@/shared/model/plani-master.model';

import PlaniMasterService from './plani-master.service';

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
  describe('PlaniMaster Service', () => {
    let service: PlaniMasterService;
    let elemDefault;
    let currentDate: Date;

    beforeEach(() => {
      service = new PlaniMasterService();
      currentDate = new Date();
      elemDefault = new PlaniMaster(123, currentDate, 0, false, false, false, false, 'AAAAAAA', 'AAAAAAA', false);
    });

    describe('Service methods', () => {
      it('should find an element', async () => {
        const returnedFromService = { fecha: dayjs(currentDate).format(DATE_FORMAT), ...elemDefault };
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

      it('should create a PlaniMaster', async () => {
        const returnedFromService = { id: 123, fecha: dayjs(currentDate).format(DATE_FORMAT), ...elemDefault };
        const expected = { fecha: currentDate, ...returnedFromService };

        axiosStub.post.resolves({ data: returnedFromService });
        return service.create({}).then(res => {
          expect(res).toMatchObject(expected);
        });
      });

      it('should not create a PlaniMaster', async () => {
        axiosStub.post.rejects(error);

        return service
          .create({})
          .then()
          .catch(err => {
            expect(err).toMatchObject(error);
          });
      });

      it('should update a PlaniMaster', async () => {
        const returnedFromService = {
          fecha: dayjs(currentDate).format(DATE_FORMAT),
          ofrenda: 1,
          visitaCordinador: true,
          visitaTutor: true,
          visitaDirector: true,
          otraVisita: true,
          note: 'BBBBBB',
          doneby: 'BBBBBB',
          completado: true,
          ...elemDefault,
        };

        const expected = { fecha: currentDate, ...returnedFromService };
        axiosStub.put.resolves({ data: returnedFromService });

        return service.update(expected).then(res => {
          expect(res).toMatchObject(expected);
        });
      });

      it('should not update a PlaniMaster', async () => {
        axiosStub.put.rejects(error);

        return service
          .update({})
          .then()
          .catch(err => {
            expect(err).toMatchObject(error);
          });
      });

      it('should partial update a PlaniMaster', async () => {
        const patchObject = { visitaTutor: true, doneby: 'BBBBBB', completado: true, ...new PlaniMaster() };
        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = { fecha: currentDate, ...returnedFromService };
        axiosStub.patch.resolves({ data: returnedFromService });

        return service.partialUpdate(patchObject).then(res => {
          expect(res).toMatchObject(expected);
        });
      });

      it('should not partial update a PlaniMaster', async () => {
        axiosStub.patch.rejects(error);

        return service
          .partialUpdate({})
          .then()
          .catch(err => {
            expect(err).toMatchObject(error);
          });
      });

      it('should return a list of PlaniMaster', async () => {
        const returnedFromService = {
          fecha: dayjs(currentDate).format(DATE_FORMAT),
          ofrenda: 1,
          visitaCordinador: true,
          visitaTutor: true,
          visitaDirector: true,
          otraVisita: true,
          note: 'BBBBBB',
          doneby: 'BBBBBB',
          completado: true,
          ...elemDefault,
        };
        const expected = { fecha: currentDate, ...returnedFromService };
        axiosStub.get.resolves([returnedFromService]);
        return service.retrieve({ sort: {}, page: 0, size: 10 }).then(res => {
          expect(res).toContainEqual(expected);
        });
      });

      it('should not return a list of PlaniMaster', async () => {
        axiosStub.get.rejects(error);

        return service
          .retrieve()
          .then()
          .catch(err => {
            expect(err).toMatchObject(error);
          });
      });

      it('should delete a PlaniMaster', async () => {
        axiosStub.delete.resolves({ ok: true });
        return service.delete(123).then(res => {
          expect(res.ok).toBeTruthy();
        });
      });

      it('should not delete a PlaniMaster', async () => {
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
