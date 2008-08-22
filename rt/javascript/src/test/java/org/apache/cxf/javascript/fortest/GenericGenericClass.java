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
name|javascript
operator|.
name|fortest
package|;
end_package

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|annotation
operator|.
name|XmlType
import|;
end_import

begin_comment
comment|/**  *   */
end_comment

begin_class
annotation|@
name|XmlType
argument_list|(
name|namespace
operator|=
literal|"uri:org.apache.cxf.javascript.testns"
argument_list|)
specifier|public
class|class
name|GenericGenericClass
parameter_list|<
name|T
parameter_list|>
implements|implements
name|GenericInterface
argument_list|<
name|T
argument_list|>
block|{
specifier|private
name|T
name|value
decl_stmt|;
specifier|public
name|void
name|doSomethingGeneric
parameter_list|(
name|T
name|t
parameter_list|)
block|{
name|value
operator|=
name|t
expr_stmt|;
block|}
specifier|public
name|T
name|getValue
parameter_list|()
block|{
return|return
name|value
return|;
block|}
specifier|public
name|void
name|setValue
parameter_list|(
name|T
name|value
parameter_list|)
block|{
name|this
operator|.
name|value
operator|=
name|value
expr_stmt|;
block|}
block|}
end_class

end_unit

