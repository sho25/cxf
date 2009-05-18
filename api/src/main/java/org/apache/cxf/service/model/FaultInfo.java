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
name|service
operator|.
name|model
package|;
end_package

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|namespace
operator|.
name|QName
import|;
end_import

begin_class
specifier|public
class|class
name|FaultInfo
extends|extends
name|AbstractMessageContainer
block|{
specifier|private
name|QName
name|faultName
decl_stmt|;
specifier|public
name|FaultInfo
parameter_list|(
name|QName
name|fname
parameter_list|,
name|QName
name|mname
parameter_list|,
name|OperationInfo
name|info
parameter_list|)
block|{
name|super
argument_list|(
name|info
argument_list|,
name|mname
argument_list|)
expr_stmt|;
name|faultName
operator|=
name|fname
expr_stmt|;
block|}
specifier|public
name|QName
name|getFaultName
parameter_list|()
block|{
return|return
name|faultName
return|;
block|}
specifier|public
name|void
name|setFaultName
parameter_list|(
name|QName
name|fname
parameter_list|)
block|{
name|faultName
operator|=
name|fname
expr_stmt|;
block|}
specifier|public
name|int
name|hashCode
parameter_list|()
block|{
return|return
name|faultName
operator|==
literal|null
condition|?
operator|-
literal|1
else|:
name|faultName
operator|.
name|hashCode
argument_list|()
return|;
block|}
specifier|public
name|boolean
name|equals
parameter_list|(
name|Object
name|o
parameter_list|)
block|{
if|if
condition|(
name|o
operator|==
name|this
condition|)
block|{
return|return
literal|true
return|;
block|}
if|if
condition|(
operator|!
operator|(
name|o
operator|instanceof
name|FaultInfo
operator|)
condition|)
block|{
return|return
literal|false
return|;
block|}
name|FaultInfo
name|oi
init|=
operator|(
name|FaultInfo
operator|)
name|o
decl_stmt|;
return|return
name|equals
argument_list|(
name|faultName
argument_list|,
name|oi
operator|.
name|faultName
argument_list|)
operator|&&
name|super
operator|.
name|equals
argument_list|(
name|o
argument_list|)
return|;
block|}
block|}
end_class

end_unit

