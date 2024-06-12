<template>
	<div class="fater-body-show">
		<el-card shadow="never">
			<div slot="header">
				信息查询
			</div>
			<div>
				<el-form :inline="true" :model="qryForm">
					<el-form-item>
						<el-input v-model="qryForm.teamName" placeholder="输入团队名称…" autocomplete="off"></el-input>
					</el-form-item>
					<el-form-item>
						<el-input v-model="qryForm.userName" placeholder="输入成员姓名…" autocomplete="off"></el-input>
					</el-form-item>
					<el-form-item>
						<el-button type="primary" icon="el-icon-search" @click="getPageLikeInfo()"></el-button>
					</el-form-item>
				</el-form>
			</div>
		</el-card>

		<el-card shadow="never">
			<div v-if="userType == 1" slot="header">
				<el-button type="primary" size="mini" icon="el-icon-plus" @click="showAddWin()"></el-button>
			</div>
			<div>
				<el-table v-loading="loading" element-loading-text="拼命加载中" element-loading-spinner="el-icon-loading"
					element-loading-background="rgba(124, 124, 124, 0.8)" :data="pageInfos" border>
					<el-table-column align="center" type="index"></el-table-column>
					<el-table-column align="center" prop="teamName" label="社团名称"></el-table-column>
					<el-table-column align="center" prop="userName" label="成员姓名"></el-table-column>
					<el-table-column align="center" prop="userGender" label="成员性别"></el-table-column>
					<el-table-column align="center" prop="userPhone" label="成员电话"></el-table-column>
					<el-table-column align="center" prop="createTime" label="缴费时间"></el-table-column>
					<el-table-column align="center" prop="total" label="缴纳费用"></el-table-column>>
					<el-table-column v-if="userType != 2" align="center" label="操作处理">
						<template slot-scope="scope">
							<el-button icon="el-icon-edit" type="primary" size="mini"
								@click="showUpdWin(scope.row)"></el-button>
							<el-button icon="el-icon-delete" type="danger" size="mini"
								@click="delInfo(scope.row.id)"></el-button>
						</template>
					</el-table-column>
				</el-table>
				<el-pagination v-if="pageTotal > 1" style="margin-top: 15px;" @size-change="handleSizeChange"
					@current-change="handleCurrentChange" :current-page="pageIndex" :page-sizes="[5, 10, 20, 50]"
					:page-size="pageSize" layout="total, sizes, prev, pager, next, jumper" :total="totalInfo">
				</el-pagination>
			</div>
		</el-card>

		<el-dialog title="添加信息" width="600px" :visible.sync="showAddFlag">
			<el-form label-width="90px" :model="payLogsForm">
				<el-form-item label="缴纳费用">
					<el-input v-model="payLogsForm.total" placeholder="请输入缴纳费用…" autocomplete="off"></el-input>
				</el-form-item>
				<el-form-item label="收费社团">
					<el-select style="width:100%;" v-model="payLogsForm.teamId" placeholder="请输入收费社团…">
						<el-option v-for="(item, index) in teams" :key="index" :label="item.name"
							:value="item.id"></el-option>
					</el-select>
				</el-form-item>
				<el-form-item label="缴费用户">
					<el-input v-model="payLogsForm.userId" placeholder="请输入缴费用户ID…" autocomplete="off"></el-input>
				</el-form-item>
			</el-form>
			<div slot="footer" class="dialog-footer">
				<el-button @click="showAddFlag = false">取 消</el-button>
				<el-button type="primary" @click="addInfo()">确 定</el-button>
			</div>
		</el-dialog>

		<el-dialog title="修改信息" width="600px" :visible.sync="showUpdFlag">
			<el-form label-width="90px" :model="payLogsForm">
				<el-form-item label="缴纳费用">
					<el-input v-model="payLogsForm.total" placeholder="请输入缴纳费用…" autocomplete="off"></el-input>
				</el-form-item>
				<el-form-item label="收费社团">
					<el-select style="width:100%;" v-model="payLogsForm.teamId" placeholder="请输入收费社团…">
						<el-option v-for="(item, index) in teams" :key="index" :label="item.name"
							:value="item.id"></el-option>
					</el-select>
				</el-form-item>
				<el-form-item label="缴费用户">
					<el-input v-model="payLogsForm.userId" placeholder="请输入缴费用户ID…" autocomplete="off"></el-input>
				</el-form-item>
			</el-form>
			<div slot="footer" class="dialog-footer">
				<el-button @click="showUpdFlag = false">取 消</el-button>
				<el-button type="primary" @click="updInfo()">确 定</el-button>
			</div>
		</el-dialog>
	</div>
</template>

<style></style>

<script>
import {
	getAllTeamList,
	getManTeamList,
	getLoginUser,
	getPagePayLogs,
	addPayLogs,
	updPayLogs,
	delPayLogs
} from "../../api";

export default {

	data() {

		return {
			teams: [],
			userType: "",
			pageInfos: [],
			pageIndex: 1,
			pageSize: 10,
			pageTotal: 0,
			totalInfo: 0,
			loading: true,
			showAddFlag: false,
			showUpdFlag: false,
			qryForm: {
				token: this.$store.state.token,
				teamName: "",
				userName: "",
			},
			payLogsForm: {
				id: "",
				total: "",
				teamId: "",
				userId: "",
			}
		}
	},
	methods: {

		getPageInfo(pageIndex, pageSize) {

			getPagePayLogs(pageIndex, pageSize, this.qryForm.token).then(resp => {

				this.pageInfos = resp.data.data;
				this.pageIndex = resp.data.pageIndex;
				this.pageSize = resp.data.pageSize;
				this.pageTotal = resp.data.pageTotal;
				this.totalInfo = resp.data.count;

				this.loading = false;
			});
		},
		getPageLikeInfo() {

			getPagePayLogs(1, this.pageSize, this.qryForm.token, this.qryForm.teamName, this.qryForm.userName).then(resp => {

				this.pageInfos = resp.data.data;
				this.pageIndex = resp.data.pageIndex;
				this.pageSize = resp.data.pageSize;
				this.totalInfo = resp.data.count;
				this.pageTotal = resp.data.pageTotal;
				this.loading = false;
			});
		},
		handleSizeChange(pageSize) {

			this.getPageInfo(this.pageIndex, pageSize, this.qryForm.token, this.qryForm.teamName, this.qryForm.userName);
		},
		handleCurrentChange(pageIndex) {

			this.getPageInfo(pageIndex, this.pageSize, this.qryForm.token, this.qryForm.teamName, this.qryForm.userName);
		},
		initForm() {

			this.payLogsForm = {
				id: "",
				total: "",
				teamId: "",
				userId: "",
			};
		},
		showAddWin() {

			this.initForm();
			this.showAddFlag = true;
		},
		showUpdWin(row) {

			this.payLogsForm = row;
			this.showUpdFlag = true;
		},
		addInfo() {

			addPayLogs(this.payLogsForm).then(resp => {

				this.$message({
					message: resp.msg,
					type: 'success'
				});

				this.getPageInfo(1, this.pageSize, this.qryForm.token);

				this.showAddFlag = false;
			});
		},
		updInfo() {

			updPayLogs(this.payLogsForm).then(resp => {

				this.$message({
					message: resp.msg,
					type: 'success'
				});

				this.getPageInfo(1, this.pageSize, this.qryForm.token);

				this.showUpdFlag = false;
			});
		},
		delInfo(id) {
			this.$confirm('即将删除相关信息, 是否继续?', '提示', {
				confirmButtonText: '确定',
				cancelButtonText: '取消',
				type: 'warning'
			}).then(() => {

				delPayLogs(id).then(resp => {

					this.$message({
						message: resp.msg,
						type: 'success'
					});

					this.getPageInfo(1, this.pageSize);
				});
			});
		}
	},
	mounted() {

		this.getPageInfo(1, this.pageSize, this.qryForm.token);

		getLoginUser(this.$store.state.token).then(resp => {

			this.userType = resp.data.type;

			if (resp.data.type == 1) {

				getManTeamList(resp.data.id).then(resp => {

					this.teams = resp.data;
				});
			} else {

				getAllTeamList().then(resp => {

					this.teams = resp.data;
				});
			}
		});
	}
}
</script>