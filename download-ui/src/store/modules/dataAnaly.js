const analy = {
	namespaced: true,
	state: {
		yearList: []
	},
	mutations: {
		updateYear(state, list) {
			state.yearList = list;
		}
	}
};

export default analy;