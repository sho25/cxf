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

begin_comment
comment|//import javax.xml.bind.annotation.XmlRootElement;
end_comment

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
comment|/**  *  */
end_comment

begin_comment
comment|//@XmlRootElement(name = "InheritanceTestDerived")
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
name|InheritanceTestDerived
extends|extends
name|InheritanceTestBase
block|{
specifier|private
name|String
name|derived
decl_stmt|;
specifier|public
name|String
name|getDerived
parameter_list|()
block|{
return|return
name|derived
return|;
block|}
specifier|public
name|void
name|setDerived
parameter_list|(
name|String
name|derived
parameter_list|)
block|{
name|this
operator|.
name|derived
operator|=
name|derived
expr_stmt|;
block|}
block|}
end_class

end_unit

