<template>
	<div class="fater-body-show">
		<el-card shadow="never">
			<div slot="header">
				信息查询
			</div>
			<div>
				<el-form :inline="true" :model="qryForm">
					<el-form-item >
						<el-input v-model="qryForm.title"
							placeholder="输入通知标题…"
							autocomplete="off"></el-input>
					</el-form-item>
					<el-form-item >
						<el-input v-model="qryForm.teamName"
							placeholder="输入团队名称"
							autocomplete="off"></el-input>
					</el-form-item>
					<el-form-item>
						<el-button type="primary" 
							icon="el-icon-search" @click="getPageLikeInfo()"></el-button>
					</el-form-item>
				</el-form>
			</div>
		</el-card>

		<el-card shadow="never">
			<div v-if="userType != 2" slot="header">
				<el-button type="primary" size="mini" 
						icon="el-icon-plus" @click="showAddWin()"></el-button>
			</div>
			<div>
				<el-table v-loading="loading" element-loading-text="拼命加载中" element-loading-spinner="el-icon-loading"
					element-loading-background="rgba(124, 124, 124, 0.8)" :data="pageInfos" border>
					<el-table-column align="center" type="index"></el-table-column>
					<el-table-column align="center" prop="title" label="通知标题"></el-table-column>
					<el-table-column align="center" prop="teamId" label="发布社团">
						<template slot-scope="scope">
							<el-tag v-if="scope.row.teamName" type="success">{{scope.row.teamName}}</el-tag>
							<el-tag v-else type="warning">系统通知</el-tag>
						</template>
					</el-table-column>
					<el-table-column align="center" prop="createTime" label="发布时间"></el-table-column>
					<el-table-column align="center" prop="detail" label="通知详情"></el-table-column>
					<el-table-column v-if="userType == 0" align="center" label="操作处理">
						<template slot-scope="scope">
							<el-button icon="el-icon-delete" type="danger" size="mini"
								@click="delInfo(scope.row.id)"></el-button>
						</template>
					</el-table-column>
					<el-table-column v-if="userType == 1" align="center" label="操作处理">
						<template slot-scope="scope">
							<el-button v-if="scope.row.teamId" icon="el-icon-delete" type="danger" size="mini"
								@click="delInfo(scope.row.id)"></el-button>
							<el-button v-else icon="el-icon-delete" type="danger" size="mini"
								disabled></el-button>
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
			<el-form label-width="90px" :model="noticesForm">
				<el-form-item label="通知标题">
					<el-input v-model="noticesForm.title" 
						placeholder="请输入通知标题…" autocomplete="off"></el-input>
				</el-form-item>
				<el-form-item v-if="userType == 1" label="发布社团">
					<el-select style="width:100%;" v-model="noticesForm.teamId" placeholder="请选择发布社团…">
						<el-option v-for="(item, index) in teams" 
							:key="index" :label="item.name" :value="item.id"></el-option>
					</el-select>
				</el-form-item>
				<el-form-item label="通知详情">
					<el-input type="textarea" rows="5" v-model="noticesForm.detail" 
						placeholder="请输入通知详情…" autocomplete="off"></el-input>
				</el-form-item>
			</el-form>
			<div slot="footer" class="dialog-footer">
				<el-button @click="showAddFlag = false">取 消</el-button>
				<el-button type="primary" @click="addInfo()">确 定</el-button>
			</div>
		</el-dialog>
	</div>
</template>

<style>

</style>

<script>
	import {
		getManTeamList,
		getLoginUser,
		getPageNotices,
		addNotices,
		delNotices
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
				qryForm: {
					token: this.$store.state.token,
					teamName: "",
					title: "",
				},
				noticesForm: {
					id: "",
					title: "",
					detail: "",
					teamId: null,
				}
			}
		},
		methods: {

			getPageInfo(pageIndex, pageSize) {

				getPageNotices(pageIndex, pageSize, this.qryForm.token).then(resp => {

					this.pageInfos = resp.data.data;
					this.pageIndex = resp.data.pageIndex;
					this.pageSize = resp.data.pageSize;
					this.pageTotal = resp.data.pageTotal;
					this.totalInfo = resp.data.count;

					this.loading = false;
				});
			},
			getPageLikeInfo() {

				getPageNotices(1, this.pageSize, this.qryForm.token, this.qryForm.title, this.qryForm.teamName).then(resp => {

					this.pageInfos = resp.data.data;
					this.pageIndex = resp.data.pageIndex;
					this.pageSize = resp.data.pageSize;
					this.totalInfo = resp.data.count;
					this.pageTotal = resp.data.pageTotal;
					this.loading = false;
				});
			},
			handleSizeChange(pageSize) {

				this.getPageInfo(this.pageIndex, pageSize, this.qryForm.token, this.qryForm.title, this.qryForm.teamName);
			},
			handleCurrentChange(pageIndex) {

				this.getPageInfo(pageIndex, this.pageSize, this.qryForm.token, this.qryForm.title, this.qryForm.teamName);
			},
			initForm() {

				this.noticesForm = {
					id: "",
					title: "",
					detail: "",
					teamId: null,
				};
			},
			showAddWin() {
				
				this.initForm();
				this.showAddFlag = true;
			},
			addInfo() {

				addNotices(this.noticesForm).then(resp => {

					this.$message({
						message: resp.msg,
						type: 'success'
					});

					this.getPageInfo(1, this.pageSize, this.qryForm.token);

					this.showAddFlag = false;
				});
			},
			delInfo(id){
				this.$confirm('即将删除相关信息, 是否继续?', '提示', {
					confirmButtonText: '确定',
					cancelButtonText: '取消',
					type: 'warning'
				}).then(() => {

					delNotices(id).then(resp => {

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

			getLoginUser(this.$store.state.token).then(resp =>{

				this.userType = resp.data.type;

				getManTeamList(resp.data.id).then(resp =>{

					this.teams = resp.data;
				});
			});
		}
	}
</script>