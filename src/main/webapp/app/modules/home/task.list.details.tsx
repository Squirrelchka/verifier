import React from 'react';

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
    </div>
  );
}

export default TaskListDetails;
