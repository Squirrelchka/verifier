import React, {useEffect} from 'react';
import {Link, useNavigate, useParams} from 'react-router-dom';
import {Button, Col, Row} from 'reactstrap';
import {Translate, translate, ValidatedField, ValidatedForm} from 'react-jhipster';
import {FontAwesomeIcon} from '@fortawesome/react-fontawesome';
import {useAppDispatch, useAppSelector} from 'app/config/store';
import {getUsers} from 'app/modules/administration/user-management/user-management.reducer';
import {getEntities as getTasks} from 'app/entities/task/task.reducer';
import {createEntity, getEntity, updateEntity} from './user-task.reducer';

export const UserTaskUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const users = useAppSelector(state => state.userManagement.users);
  const tasks = useAppSelector(state => state.task.entities);
  const userTaskEntity = useAppSelector(state => state.userTask.entity);
  const loading = useAppSelector(state => state.userTask.loading);
  const updating = useAppSelector(state => state.userTask.updating);
  const updateSuccess = useAppSelector(state => state.userTask.updateSuccess);

  const handleClose = () => {
    navigate('/user-task');
  };

  useEffect(() => {
    if (!isNew) {
      dispatch(getEntity(id));
    }

    dispatch(getUsers({}));
    dispatch(getTasks({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    const entity = {
      ...userTaskEntity,
      ...values,
      user: users.find(it => it.id.toString() === values.user.toString()),
      task: tasks.find(it => it.id.toString() === values.task.toString()),
    };

    if (isNew) {
      dispatch(createEntity(entity));
    } else {
      dispatch(updateEntity(entity));
    }
  };

  const defaultValues = () =>
    isNew
      ? {}
      : {
          ...userTaskEntity,
          user: userTaskEntity?.user?.id,
          task: userTaskEntity?.task?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="sqlverifierApp.userTask.home.createOrEditLabel" data-cy="UserTaskCreateUpdateHeading">
            <Translate contentKey="sqlverifierApp.userTask.home.createOrEditLabel">Create or edit a UserTask</Translate>
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
              <ValidatedField
                label={translate('sqlverifierApp.userTask.isSolved')}
                id="user-task-isSolved"
                name="isSolved"
                data-cy="isSolved"
                check
                type="checkbox"
              />
              <ValidatedField
                id="user-task-user"
                name="user"
                data-cy="user"
                label={translate('sqlverifierApp.userTask.user')}
                type="select"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              >
                <option value="" key="0" />
                {users
                  ? users.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.login}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField
                id="user-task-task"
                name="task"
                data-cy="task"
                label={translate('sqlverifierApp.userTask.task')}
                type="select"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              >
                <option value="" key="0" />
                {tasks
                  ? tasks.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.title}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/user-task" replace color="info">
                <FontAwesomeIcon icon="arrow-left" />
                &nbsp;
                <span className="d-none d-md-inline">
                  <Translate contentKey="entity.action.back">Back</Translate>
                </span>
              </Button>
              &nbsp;
              <Button color="primary" id="save-entity" data-cy="entityCreateSaveButton" type="submit" disabled={updating}>
                <FontAwesomeIcon icon="save" />
                &nbsp;
                <Translate contentKey="entity.action.save">Save</Translate>
              </Button>
            </ValidatedForm>
          )}
        </Col>
      </Row>
    </div>
  );
};

export default UserTaskUpdate;
