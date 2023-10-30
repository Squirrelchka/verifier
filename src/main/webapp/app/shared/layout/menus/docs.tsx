import React from 'react';
import MenuItem from 'app/shared/layout/menus/menu-item';
import {NavDropdown} from './menu-components';
import {Translate, translate} from 'react-jhipster';

const openAPIItem = () => (
  <MenuItem icon="book" to="/docs/docs">
    <Translate contentKey="global.menu.apidocs.apidocs">API</Translate>
  </MenuItem>
);

export const DocsMenu = () => (
  <NavDropdown icon="users-cog" name={translate('global.menu.apidocs.swagger')} id="docs-menu" data-cy="docsMenu">
    {openAPIItem()}
  </NavDropdown>
);

export default DocsMenu;
