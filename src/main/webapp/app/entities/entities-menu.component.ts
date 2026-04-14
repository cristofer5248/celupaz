// import { defineComponent } from 'vue';
// import { useI18n } from 'vue-i18n';

// export default defineComponent({
//   name: 'EntitiesMenu',
//   setup() {
//     const i18n = useI18n();
//     return {
//       t$: i18n.t,
//     };
//   },
// });
import { defineComponent, inject, computed } from 'vue'; // Añadimos computed
import { useI18n } from 'vue-i18n';

export default defineComponent({
  compatConfig: { MODE: 3 },
  name: 'EntitiesMenu',
  setup() {
    const t$ = useI18n().t;
    const accountService = inject<any>('accountService');

    // Usamos una propiedad computada para que Vue reaccione cuando el usuario se loguee
    const hasAnyAuthority = (authorities: string[] | string): boolean => {
      // 1. Verificamos si el servicio existe
      if (!accountService) return false;

      // 2. Si el usuario no está autenticado, no ve nada
      if (!accountService.authenticated) return false;

      // 3. Intento de usar el método nativo de JHipster
      if (typeof accountService.hasAnyAuthority === 'function') {
        return accountService.hasAnyAuthority(authorities);
      }

      // 4. Plan B: Si lo anterior falla, revisamos los roles manualmente en el store
      const userAuthorities = accountService.userAuthorities || [];
      if (typeof authorities === 'string') {
        return userAuthorities.includes(authorities);
      } else {
        return authorities.some(auth => userAuthorities.includes(auth));
      }
    };

    return {
      hasAnyAuthority,
      t$,
    };
  },
});
