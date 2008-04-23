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
name|systest
operator|.
name|coloc
package|;
end_package

begin_class
specifier|public
specifier|final
class|class
name|HeaderTesterUtil
block|{
specifier|public
specifier|static
specifier|final
name|String
name|IN_MESSAGE
init|=
literal|"in message"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|IN_ORIGINATOR
init|=
literal|"in originator"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|IN_REQUEST_TYPE
init|=
literal|"in request type"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|OUT_MESSAGE_IN
init|=
literal|"out message in"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|OUT_MESSAGE_OUT
init|=
literal|"out message out"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|OUT_ORIGINATOR_IN
init|=
literal|"out orginator in"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|OUT_ORIGINATOR_OUT
init|=
literal|"out orginator out"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|OUT_REQUEST_TYPE
init|=
literal|"out request test"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|OUT_RESPONSE_TYPE
init|=
literal|"out Header type"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|INOUT_MESSAGE_IN
init|=
literal|"inout message in"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|INOUT_MESSAGE_OUT
init|=
literal|"inout message out"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|INOUT_ORIGINATOR_IN
init|=
literal|"inout originator in"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|INOUT_ORIGINATOR_OUT
init|=
literal|"inout orginator out"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|INOUT_REQUEST_TYPE_IN
init|=
literal|"inout request type in"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|INOUT_REQUEST_TYPE_OUT
init|=
literal|"inout request type out"
decl_stmt|;
specifier|static
specifier|final
name|String
name|EX_STRING
init|=
literal|"CXF RUNTIME EXCEPTION"
decl_stmt|;
specifier|private
name|HeaderTesterUtil
parameter_list|()
block|{
comment|//utility class
block|}
block|}
end_class

end_unit

