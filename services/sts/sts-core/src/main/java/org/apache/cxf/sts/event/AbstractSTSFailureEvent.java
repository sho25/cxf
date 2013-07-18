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
name|sts
operator|.
name|event
package|;
end_package

begin_class
specifier|public
specifier|abstract
class|class
name|AbstractSTSFailureEvent
extends|extends
name|AbstractSTSEvent
block|{
comment|//private static final String OPERATION = "Issue";
specifier|private
name|Exception
name|exception
decl_stmt|;
specifier|public
name|AbstractSTSFailureEvent
parameter_list|(
name|Object
name|source
parameter_list|,
name|long
name|duration
parameter_list|,
name|Exception
name|ex
parameter_list|)
block|{
name|super
argument_list|(
name|source
argument_list|,
name|duration
argument_list|)
expr_stmt|;
name|exception
operator|=
name|ex
expr_stmt|;
block|}
specifier|public
name|Exception
name|getException
parameter_list|()
block|{
return|return
name|exception
return|;
block|}
comment|/*@Override     public String getOperation() {         return OPERATION;     }*/
block|}
end_class

end_unit

