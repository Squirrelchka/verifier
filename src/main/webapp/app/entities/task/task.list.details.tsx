import React from 'react';
import {Link} from 'react-router-dom';
import {Button} from 'reactstrap';
import {Translate} from 'react-jhipster';
import {FontAwesomeIcon} from '@fortawesome/react-fontawesome';

function TaskListDetails(props) {
  const {id, title, text, paginationState} = props;
  return (
    <div>
      <h3>{title}</h3>
      <textarea
        placeholder={text}
        style={{width: '100%'}}
        rows={4}
        readOnly
      >
      </textarea>
      <div style={{ display: 'flex', justifyContent: 'flex-end' }}>
        <Button tag={Link} to={`/task/${id}`} color="info" size="sm" data-cy="entityDetailsButton">
          <FontAwesomeIcon icon="eye"/>{' '}
          <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
        </Button>
        <Button
          tag={Link}
          to={`/task/${id}/edit?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`}
          color="primary"
          size="sm"
          data-cy="entityEditButton"
        >
          <FontAwesomeIcon icon="pencil-alt"/>{' '}
          <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.edit">Edit</Translate>
                        </span>
        </Button>
        <Button
          tag={Link}
          to={`/task/${id}/delete?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`}
          color="danger"
          size="sm"
          data-cy="entityDeleteButton"
        >
          <FontAwesomeIcon icon="trash"/>{' '}
          <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.delete">Delete</Translate>
                        </span>
        </Button>
      </div>
    </div>
  );
}

export default TaskListDetails;
