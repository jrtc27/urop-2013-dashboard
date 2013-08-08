moduleScripts['dashboard'] = {
	'home' : {
		'index': [
            bindNotificationShowMoreListener,
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
			bindNotificationShowMoreListener,
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
			applyDatepicker,
			userTokenInput,		
		],
		'edit' : [
			editGroup,
			applyDatepicker,
			userTokenInput
		]
	},
	'deadlines' : {
		'index' : [
			deadlinesIndex
		],
		'manage' : [
			function(){this.foundation()},
			editDeadline,
			applyDatepicker,
			userTokenInput,
			
		],
		'edit' : [
			editDeadline,
			applyDatepicker,
			userTokenInput
		]
	},
	'supervisor' : {
		'index' : [
			createDeadline,	
	        deleteDeadline,
	      	createGroup,
	      	importGroup,
	        deleteGroup,
	        applyDatepicker,
			userTokenInput,
			userSurnameTokenInput,
			groupTokenInput,
			groupImportTokenInput,
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
	        groupTokenInput,
			userTokenInput	
		],
		'newgroup' : [
			createGroup,	
	      	importGroup,
	        applyDatepicker,
			userTokenInput,
			userSurnameTokenInput,
			groupImportTokenInput, 
			tokenInputType	
		]
	}
};
