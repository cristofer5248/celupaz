<template>
  <div>
    <b-dropdown-item to="/alma">
      <font-awesome-icon icon="asterisk" />
      <span>{{ t$('global.menu.entities.alma') }}</span>
    </b-dropdown-item>
    <b-dropdown-item to="/cell-type">
      <font-awesome-icon icon="asterisk" />
      <span>{{ t$('global.menu.entities.cellType') }}</span>
    </b-dropdown-item>
    <b-dropdown-item to="/cell">
      <font-awesome-icon icon="asterisk" />
      <span>{{ t$('global.menu.entities.cell') }}</span>
    </b-dropdown-item>
    <b-dropdown-item to="/member">
      <font-awesome-icon icon="asterisk" />
      <span>{{ t$('global.menu.entities.member') }}</span>
    </b-dropdown-item>
    <b-dropdown-item to="/iglesia">
      <font-awesome-icon icon="asterisk" />
      <span>{{ t$('global.menu.entities.iglesia') }}</span>
    </b-dropdown-item>
    <b-dropdown-item to="/privilege">
      <font-awesome-icon icon="asterisk" />
      <span>{{ t$('global.menu.entities.privilege') }}</span>
    </b-dropdown-item>
    <b-dropdown-item to="/alma-history">
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
    <b-dropdown-item to="/member-celula">
      <font-awesome-icon icon="asterisk" />
      <span>{{ t$('global.menu.entities.memberCelula') }}</span>
    </b-dropdown-item>
    <b-dropdown-item to="/plani-master">
      <font-awesome-icon icon="asterisk" />
      <span>{{ t$('global.menu.entities.planiMaster') }}</span>
    </b-dropdown-item>
    <b-dropdown-item to="/rol-celula">
      <font-awesome-icon icon="asterisk" />
      <span>{{ t$('global.menu.entities.rolCelula') }}</span>
    </b-dropdown-item>
    <!-- jhipster-needle-add-entity-to-menu - JHipster will add entities to the menu here -->
  </div>
</template>

<script lang="ts" src="./entities-menu.component.ts"></script>
