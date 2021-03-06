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
name|aegis
operator|.
name|services
package|;
end_package

begin_import
import|import
name|javax
operator|.
name|jws
operator|.
name|WebMethod
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|jws
operator|.
name|WebService
import|;
end_import

begin_comment
comment|/**  * An array service for testing.  * There are some JAX-WS annotations for tests with JAX-WS.  */
end_comment

begin_class
annotation|@
name|WebService
argument_list|(
name|targetNamespace
operator|=
literal|"urn:org.apache.cxf.aegis"
argument_list|,
name|serviceName
operator|=
literal|"arrayService"
argument_list|)
specifier|public
class|class
name|ArrayService
block|{
specifier|private
name|org
operator|.
name|jdom
operator|.
name|Element
index|[]
name|jdomArray
decl_stmt|;
specifier|private
name|org
operator|.
name|w3c
operator|.
name|dom
operator|.
name|Document
index|[]
name|w3cArray
decl_stmt|;
specifier|private
name|String
name|beforeValue
decl_stmt|;
specifier|private
name|String
name|afterValue
decl_stmt|;
specifier|private
name|Number
name|numberValue
decl_stmt|;
specifier|public
name|ArrayService
parameter_list|()
block|{     }
annotation|@
name|WebMethod
specifier|public
name|SimpleBean
index|[]
name|getBeanArray
parameter_list|()
block|{
name|SimpleBean
name|bean
init|=
operator|new
name|SimpleBean
argument_list|()
decl_stmt|;
name|bean
operator|.
name|setBleh
argument_list|(
literal|"bleh"
argument_list|)
expr_stmt|;
name|bean
operator|.
name|setHowdy
argument_list|(
literal|"howdy"
argument_list|)
expr_stmt|;
return|return
operator|new
name|SimpleBean
index|[]
block|{
name|bean
block|}
return|;
block|}
annotation|@
name|WebMethod
specifier|public
name|void
name|takeOneSimpleBean
parameter_list|(
name|SimpleBean
name|sb
parameter_list|)
block|{     }
specifier|public
name|void
name|resetValues
parameter_list|()
block|{
name|beforeValue
operator|=
literal|null
expr_stmt|;
name|afterValue
operator|=
literal|null
expr_stmt|;
name|jdomArray
operator|=
literal|null
expr_stmt|;
name|w3cArray
operator|=
literal|null
expr_stmt|;
block|}
annotation|@
name|WebMethod
specifier|public
name|String
index|[]
name|getStringArray
parameter_list|()
block|{
return|return
operator|new
name|String
index|[]
block|{
literal|"bleh"
block|,
literal|"bleh"
block|}
return|;
block|}
annotation|@
name|WebMethod
specifier|public
name|boolean
name|submitStringArray
parameter_list|(
name|String
index|[]
name|array
parameter_list|)
block|{
return|return
literal|true
return|;
block|}
annotation|@
name|WebMethod
specifier|public
name|boolean
name|submitBeanArray
parameter_list|(
name|SimpleBean
index|[]
name|array
parameter_list|)
block|{
return|return
literal|true
return|;
block|}
annotation|@
name|WebMethod
specifier|public
name|void
name|verifyCustomParamName
parameter_list|(
name|String
name|param
parameter_list|)
block|{     }
annotation|@
name|WebMethod
specifier|public
name|void
name|submitJDOMArray
parameter_list|(
name|String
name|before
parameter_list|,
name|org
operator|.
name|jdom
operator|.
name|Element
index|[]
name|anything
parameter_list|,
name|String
name|after
parameter_list|)
block|{
name|beforeValue
operator|=
name|before
expr_stmt|;
name|jdomArray
operator|=
name|anything
expr_stmt|;
name|afterValue
operator|=
name|after
expr_stmt|;
block|}
annotation|@
name|WebMethod
specifier|public
name|void
name|submitW3CArray
parameter_list|(
name|String
name|before
parameter_list|,
name|org
operator|.
name|w3c
operator|.
name|dom
operator|.
name|Document
index|[]
name|anything
parameter_list|,
name|String
name|after
parameter_list|)
block|{
name|beforeValue
operator|=
name|before
expr_stmt|;
name|w3cArray
operator|=
name|anything
expr_stmt|;
name|afterValue
operator|=
name|after
expr_stmt|;
block|}
annotation|@
name|WebMethod
specifier|public
name|org
operator|.
name|jdom
operator|.
name|Element
index|[]
name|getJdomArray
parameter_list|()
block|{
return|return
name|jdomArray
return|;
block|}
specifier|public
name|org
operator|.
name|w3c
operator|.
name|dom
operator|.
name|Document
index|[]
name|getW3cArray
parameter_list|()
block|{
return|return
name|w3cArray
return|;
block|}
specifier|public
name|void
name|takeNumber
parameter_list|(
name|Number
name|numberParam
parameter_list|)
block|{
name|numberValue
operator|=
name|numberParam
expr_stmt|;
block|}
specifier|public
name|Number
name|getNumberValue
parameter_list|()
block|{
return|return
name|numberValue
return|;
block|}
specifier|public
name|String
name|getBeforeValue
parameter_list|()
block|{
return|return
name|beforeValue
return|;
block|}
specifier|public
name|String
name|getAfterValue
parameter_list|()
block|{
return|return
name|afterValue
return|;
block|}
block|}
end_class

end_unit

