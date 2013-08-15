moduleScripts['dashboard'] = {
	'home' : {
		'index': [
            bindPaginationShowMoreListener,
            markNotificationAsReadUnread
        ]
	},
	'account' : {
		'index': [
            bindSaveSettingsListener
		 ]
	},
	'notifications' : {
		'index': [
			bindPaginationShowMoreListener,
            markNotificationAsReadUnread
		]
	},
	'shared' : {
		'getNotifications': [
            markNotificationAsReadUnread
        ]
	},
	'groups' : {
		'index': [
			groupsIndex
		],
		'manage' : [
			editGroup,
			gUserTokenInput,
			tokenInputType
		],
		'edit' : [
			editGroup,
			gUserTokenInput,
			tokenInputType
		]
	},
	'deadlines' : {
		'index' : [
			deadlinesIndex,
			bindPaginationShowMoreListener
		],
		'manage' : [
			editDeadline,
			applyDatepicker,
			dUserTokenInput,
			dGroupTokenInput,
			tokenInputType
		],
		'edit' : [
			editDeadline,
			applyDatepicker,
			dUserTokenInput,
			dGroupTokenInput,
			tokenInputType
		]
	},
	'supervisor' : {
		'index' : [
		    tabMemory,
			createDeadline,	
	        deleteDeadline,
	      	createGroup,
	      	importGroup,
	        deleteGroup,
	        applyDatepicker,
	        dUserTokenInput,
			dGroupTokenInput,
			gUserTokenInput,
			gGroupTokenInput,
			tokenInputType
		],
		'deadline' : [
			deleteDeadline
		],
		'group' : [
			deleteGroup
		],
		'newdeadline' : [
			createDeadline,	
	        applyDatepicker,
			dUserTokenInput,
			dGroupTokenInput,
			tokenInputType
		],
		'newgroup' : [
			createGroup,	
	        applyDatepicker,
			gUserTokenInput,
			tokenInputType
		], 
		'importgroup' : [
		    importGroup,
		    gGroupTokenInput
		],
	}
};
