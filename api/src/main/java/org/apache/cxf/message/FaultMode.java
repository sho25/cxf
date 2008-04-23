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
name|message
package|;
end_package

begin_comment
comment|/**  *   */
end_comment

begin_enum
specifier|public
enum|enum
name|FaultMode
block|{
name|RUNTIME_FAULT
argument_list|(
literal|"org.apache.cxf.runtime.fault"
argument_list|)
block|,
name|LOGICAL_RUNTIME_FAULT
argument_list|(
literal|"org.apache.cxf.runtime.fault.logical"
argument_list|)
block|,
name|CHECKED_APPLICATION_FAULT
argument_list|(
literal|"org.apache.cxf.application.fault.checked"
argument_list|)
block|,
name|UNCHECKED_APPLICATION_FAULT
argument_list|(
literal|"org.apache.cxf.application.fault.unchecked"
argument_list|)
block|;
name|String
name|mode
decl_stmt|;
name|FaultMode
parameter_list|(
name|String
name|m
parameter_list|)
block|{
name|mode
operator|=
name|m
expr_stmt|;
block|}
block|}
end_enum

end_unit

