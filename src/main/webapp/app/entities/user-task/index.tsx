import React from 'react';
import {Route} from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import UserTask from './user-task';
import UserTaskDetail from './user-task-detail';
import UserTaskUpdate from './user-task-update';
import UserTaskDeleteDialog from './user-task-delete-dialog';

const UserTaskRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<UserTask />} />
    <Route path="new" element={<UserTaskUpdate />} />
    <Route path=":id">
      <Route index element={<UserTaskDetail />} />
      <Route path="edit" element={<UserTaskUpdate />} />
      <Route path="delete" element={<UserTaskDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default UserTaskRoutes;
