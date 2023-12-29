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
      <MenuItem icon="asterisk" to="/file">
        <Translate contentKey="global.menu.entities.file" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/file-belongings">
        <Translate contentKey="global.menu.entities.fileBelongings" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/version">
        <Translate contentKey="global.menu.entities.version" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/donor">
        <Translate contentKey="global.menu.entities.donor" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/center">
        <Translate contentKey="global.menu.entities.center" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/center-donor">
        <Translate contentKey="global.menu.entities.centerDonor" />
      </MenuItem>
      {/* jhipster-needle-add-entity-to-menu - JHipster will add entities to the menu here */}
    </>
  );
};

export default EntitiesMenu as React.ComponentType<any>;
