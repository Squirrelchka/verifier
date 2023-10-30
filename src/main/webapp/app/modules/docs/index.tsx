import React from 'react';

import {Route} from 'react-router-dom';
import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';
import Docs from './docs/docs';

const DocsRoutes = () => (
  <div>
    <ErrorBoundaryRoutes>
      <Route path="docs" element={<Docs />} />
    </ErrorBoundaryRoutes>
  </div>
);

export default DocsRoutes;
