<template>
  <div>
    <b-dropdown-item to="/alma" v-if="hasAnyAuthority(['ROLE_ADMIN', 'ROLE_L2'])">
      <font-awesome-icon icon="asterisk" />
      <span>{{ t$('global.menu.entities.alma') }}</span>
    </b-dropdown-item>
    <b-dropdown-item to="/cell-type" v-if="hasAnyAuthority('ROLE_ADMIN')">
      <font-awesome-icon icon="asterisk" />
      <span>{{ t$('global.menu.entities.cellType') }}</span>
    </b-dropdown-item>
    <b-dropdown-item to="/cell" v-if="hasAnyAuthority(['ROLE_ADMIN', 'ROLE_L2'])">
      <font-awesome-icon icon="asterisk" />
      <span>{{ t$('global.menu.entities.cell') }}</span>
    </b-dropdown-item>
    <b-dropdown-item to="/member">
      <font-awesome-icon icon="asterisk" />
      <span>{{ t$('global.menu.entities.member') }}</span>
    </b-dropdown-item>
    <b-dropdown-item to="/iglesia" v-if="hasAnyAuthority('ROLE_ADMIN')">
      <font-awesome-icon icon="asterisk" />
      <span>{{ t$('global.menu.entities.iglesia') }}</span>
    </b-dropdown-item>
    <b-dropdown-item to="/privilege" v-if="hasAnyAuthority('ROLE_ADMIN')">
      <font-awesome-icon icon="asterisk" />
      <span>{{ t$('global.menu.entities.privilege') }}</span>
    </b-dropdown-item>
    <b-dropdown-item to="/alma-history" v-if="hasAnyAuthority(['ROLE_ADMIN', 'ROLE_L2'])">
      <font-awesome-icon icon="asterisk" />
      <span>{{ t$('global.menu.entities.almaHistory') }}</span>
    </b-dropdown-item>
    <b-dropdown-item to="/attendance">
      <font-awesome-icon icon="asterisk" />
      <span>{{ t$('global.menu.entities.attendance') }}</span>
    </b-dropdown-item>
    <b-dropdown-item to="/planificacion">
      <font-awesome-icon icon="asterisk" />
      <span>{{ t$('global.menu.entities.planificacion') }}</span>
    </b-dropdown-item>
    <b-dropdown-item to="/member-celula" v-if="hasAnyAuthority(['ROLE_ADMIN', 'ROLE_L2'])">
      <font-awesome-icon icon="asterisk" />
      <span>{{ t$('global.menu.entities.memberCelula') }}</span>
    </b-dropdown-item>
    <b-dropdown-item to="/plani-master">
      <font-awesome-icon icon="asterisk" />
      <span>{{ t$('global.menu.entities.planiMaster') }}</span>
    </b-dropdown-item>
    <b-dropdown-item to="/rol-celula" v-if="hasAnyAuthority('ROLE_ADMIN')">
      <font-awesome-icon icon="asterisk" />
      <span>{{ t$('global.menu.entities.rolCelula') }}</span>
    </b-dropdown-item>
    <!-- jhipster-needle-add-entity-to-menu - JHipster will add entities to the menu here -->
  </div>
</template>

<script lang="ts" src="./entities-menu.component.ts"></script>
