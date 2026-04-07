<template>
  <div class="d-flex justify-content-center">
    <div class="col-8">
      <div v-if="attendance">
        <h2 class="jh-entity-heading" data-cy="attendanceDetailsHeading">
          <span>{{ t$('celupazmasterApp.attendance.detail.title') }}Attendance</span> {{ attendance.id }}
        </h2>
        <dl class="row-md jh-entity-details">
          <dt>
            <span>{{ t$('celupazmasterApp.attendance.fecha') }}</span>
          </dt>
          <dd>
            <span>{{ attendance.fecha }}</span>
          </dd>
          <dt>
            <span>{{ t$('celupazmasterApp.attendance.membercelula') }}</span>
          </dt>
          <dd>
            <div v-if="attendance.membercelula">
              <router-link :to="{ name: 'MemberCelulaView', params: { memberCelulaId: attendance.membercelula.id } }">{{
                attendance.membercelula.id
              }}</router-link>
            </div>
          </dd>
          <dt>
            <span>{{ t$('celupazmasterApp.attendance.planificacion') }}</span>
          </dt>
          <dd>
            <div v-if="attendance.planificacion">
              <router-link :to="{ name: 'PlanificacionView', params: { planificacionId: attendance.planificacion.id } }">{{
                attendance.planificacion.fecha
              }}</router-link>
            </div>
          </dd>
        </dl>
        <button type="submit" @click.prevent="previousState()" class="btn btn-info" data-cy="entityDetailsBackButton">
          <font-awesome-icon icon="arrow-left"></font-awesome-icon>&nbsp;<span>{{ t$('entity.action.back') }}</span>
        </button>
        <router-link
          v-if="attendance.id"
          :to="{ name: 'AttendanceEdit', params: { attendanceId: attendance.id } }"
          custom
          v-slot="{ navigate }"
        >
          <button @click="navigate" class="btn btn-primary">
            <font-awesome-icon icon="pencil-alt"></font-awesome-icon>&nbsp;<span>{{ t$('entity.action.edit') }}</span>
          </button>
        </router-link>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./attendance-details.component.ts"></script>
