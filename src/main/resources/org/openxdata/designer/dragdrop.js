importPackage(java.lang);
importPackage(org.apache.pivot.wtk);

var formTreeDropTarget = new DropTarget() {

	dragEnter: 
    function(component, dragContent, supportedDropActions, userDropAction) {
        var dropAction = null;
        if (dragContent.containsFileList()
            && DropAction.COPY.isSelected(supportedDropActions)) {
            dropAction = DropAction.COPY;
        }
        return dropAction;
    },

    dragExit:
    function(component) {
    },

    dragMove: 
    function(component, dragContent, supportedDropActions, x, y, userDropAction) {
        return (dragContent.containsFileList() ? DropAction.COPY : null);
    },

    userDropActionChange: 
    function(component, dragContent, supportedDropActions, x, y, userDropAction) {
        return (dragContent.containsFileList() ? DropAction.COPY : null);
    },

    drop: 
    function(component, dragContent, supportedDropActions, x, y, userDropAction) {
        var dropAction = null;
        if (dragContent.containsFileList()) {
            return application.drop(dragContent);
        }
        dragExit(component);
        return dropAction;
    }
};
