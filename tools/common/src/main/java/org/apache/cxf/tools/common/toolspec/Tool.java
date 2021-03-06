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
name|tools
operator|.
name|common
operator|.
name|toolspec
package|;
end_package

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|tools
operator|.
name|common
operator|.
name|ToolException
import|;
end_import

begin_interface
specifier|public
interface|interface
name|Tool
block|{
name|String
name|TOOL_SPEC_PUBLIC_ID
init|=
literal|"http://cxf.apache.org/Xutil/ToolSpecification"
decl_stmt|;
name|void
name|init
parameter_list|()
throws|throws
name|ToolException
function_decl|;
comment|/**      * A tool has to be prepared to perform it's duty any number of times.      */
name|void
name|performFunction
parameter_list|()
throws|throws
name|ToolException
function_decl|;
name|void
name|destroy
parameter_list|()
throws|throws
name|ToolException
function_decl|;
block|}
end_interface

end_unit

