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
name|jca
operator|.
name|inbound
package|;
end_package

begin_comment
comment|/**  *  * DispatchMDBActivationSpec is an {@link javax.resource.spi.ActivationSpec}  * that activates a CXF service endpoint facade to dispatch call to the target   * Stateless Session Bean.  *   */
end_comment

begin_class
specifier|public
class|class
name|DispatchMDBActivationSpec
extends|extends
name|MDBActivationSpec
block|{
specifier|private
name|String
name|targetBeanJndiName
decl_stmt|;
comment|/**      * @param targetBeanJndiName       */
specifier|public
name|void
name|setTargetBeanJndiName
parameter_list|(
name|String
name|targetBeanJndiName
parameter_list|)
block|{
name|this
operator|.
name|targetBeanJndiName
operator|=
name|targetBeanJndiName
expr_stmt|;
block|}
comment|/**      * @return the targetBeanJndiName      */
specifier|public
name|String
name|getTargetBeanJndiName
parameter_list|()
block|{
return|return
name|targetBeanJndiName
return|;
block|}
block|}
end_class

end_unit

