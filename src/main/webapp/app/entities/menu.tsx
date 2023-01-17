import React from 'react';
import { Translate } from 'react-jhipster';

import MenuItem from 'app/shared/layout/menus/menu-item';

const EntitiesMenu = () => {
  return (
    <>
      {/* prettier-ignore */}
      <MenuItem icon="asterisk" to="/tablet">
        <Translate contentKey="global.menu.entities.tablet" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/tablet-user">
        <Translate contentKey="global.menu.entities.tabletUser" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/user-activity">
        <Translate contentKey="global.menu.entities.userActivity" />
      </MenuItem>
      {/* jhipster-needle-add-entity-to-menu - JHipster will add entities to the menu here */}
    </>
  );
};

export default EntitiesMenu as React.ComponentType<any>;
