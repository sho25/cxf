begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements. See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership. The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License. You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing,  * software distributed under the License is distributed on an  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  * KIND, either express or implied. See the License for the  * specific language governing permissions and limitations  * under the License.  */
end_comment

begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|ws
operator|.
name|transfer
operator|.
name|shared
package|;
end_package

begin_comment
comment|/**  * Helper class for holding of constants.  */
end_comment

begin_class
specifier|public
specifier|final
class|class
name|TransferConstants
block|{
specifier|public
specifier|static
specifier|final
name|String
name|TRANSFER_2011_03_NAMESPACE
init|=
literal|"http://www.w3.org/2011/03/ws-tra"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|NAME_RESOURCE
init|=
literal|"Resource"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|NAME_RESOURCE_FACTORY
init|=
literal|"ResourceFactory"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|NAME_OPERATION_GET
init|=
literal|"Get"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|NAME_OPERATION_DELETE
init|=
literal|"Delete"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|NAME_OPERATION_PUT
init|=
literal|"Put"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|NAME_OPERATION_CREATE
init|=
literal|"Create"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|NAME_MESSAGE_GET
init|=
literal|"Get"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|NAME_MESSAGE_GET_RESPONSE
init|=
literal|"GetResponse"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|NAME_MESSAGE_DELETE
init|=
literal|"Delete"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|NAME_MESSAGE_DELETE_RESPONSE
init|=
literal|"DeleteResponse"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|NAME_MESSAGE_PUT
init|=
literal|"Put"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|NAME_MESSAGE_PUT_RESPONSE
init|=
literal|"PutResponse"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|NAME_MESSAGE_CREATE
init|=
literal|"Create"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|NAME_MESSAGE_CREATE_RESPONSE
init|=
literal|"CreateResponse"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|ACTION_GET
init|=
literal|"http://www.w3.org/2011/03/ws-tra/Get"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|ACTION_GET_RESPONSE
init|=
literal|"http://www.w3.org/2011/03/ws-tra/GetResponse"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|ACTION_DELETE
init|=
literal|"http://www.w3.org/2011/03/ws-tra/Delete"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|ACTION_DELETE_RESPONSE
init|=
literal|"http://www.w3.org/2011/03/ws-tra/DeleteResponse"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|ACTION_PUT
init|=
literal|"http://www.w3.org/2011/03/ws-tra/Put"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|ACTION_PUT_RESPONSE
init|=
literal|"http://www.w3.org/2011/03/ws-tra/PutResponse"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|ACTION_CREATE
init|=
literal|"http://www.w3.org/2011/03/ws-tra/Create"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|ACTION_CREATE_RESPONSE
init|=
literal|"http://www.w3.org/2011/03/ws-tra/CreateResponse"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|RESOURCE_REMOTE_SUFFIX
init|=
literal|"_factory"
decl_stmt|;
specifier|private
name|TransferConstants
parameter_list|()
block|{              }
block|}
end_class

end_unit
