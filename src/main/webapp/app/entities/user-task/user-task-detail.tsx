import React, {useEffect} from 'react';
import {Link, useParams} from 'react-router-dom';
import {Button, Col, Row} from 'reactstrap';
import {Translate} from 'react-jhipster';
import {FontAwesomeIcon} from '@fortawesome/react-fontawesome';

import {useAppDispatch, useAppSelector} from 'app/config/store';

import {getEntity} from './user-task.reducer';

export const UserTaskDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const userTaskEntity = useAppSelector(state => state.userTask.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="userTaskDetailsHeading">
          <Translate contentKey="sqlverifierApp.userTask.detail.title">UserTask</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{userTaskEntity.id}</dd>
          <dt>
            <span id="isSolved">
              <Translate contentKey="sqlverifierApp.userTask.isSolved">Is Solved</Translate>
            </span>
          </dt>
          <dd>{userTaskEntity.isSolved ? 'true' : 'false'}</dd>
          <dt>
            <Translate contentKey="sqlverifierApp.userTask.user">User</Translate>
          </dt>
          <dd>{userTaskEntity.user ? userTaskEntity.user.id : ''}</dd>
          <dt>
            <Translate contentKey="sqlverifierApp.userTask.task">Task</Translate>
          </dt>
          <dd>{userTaskEntity.task ? userTaskEntity.task.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/user-task" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/user-task/${userTaskEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default UserTaskDetail;
