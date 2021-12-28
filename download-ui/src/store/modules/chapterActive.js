const chapterActive = {
	namespaced: true,
	state: {
		testTypeIndex: -1,
		otherTypeIndex: -1
	},
	mutations: {
		updateTestIndex(state, val) {
			state.testTypeIndex = val;
		},
		updateOtherIndex(state, val) {
			state.otherTypeIndex = val;
		}
	}
};

export default chapterActive;