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

begin_import
import|import
name|java
operator|.
name|lang
operator|.
name|reflect
operator|.
name|Method
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|ejb
operator|.
name|MessageDrivenBean
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|ejb
operator|.
name|MessageDrivenContext
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|naming
operator|.
name|InitialContext
import|;
end_import

begin_comment
comment|/**  * DispatchMDBMessageListenerImpl supports dispatching of calls to a   * Stateless Session Bean.  *    * DispatchMDBMessageListenerImpl is intended to be used as the<ejb-class> of  * the<message-driven> bean in the resource adaptor's deployment descriptor  * (ra.xml).  When it is used, the<messaging-type> should be set to   * org.apache.cxf.jca.inbound.DispatchMDBMessageListener.  Also, the resource  * adaptor's deployment descriptor should specify the same interface  * (org.apache.cxf.jca.inbound.DispatchMDBMessageListener) in the   *<messagelistener-type> in order to activate the inbound facade endpoint.    * Since the Message Driven Bean is used to activate the inbound   * endpoint facade by CXF JCA connector, all the required resources (such as,   * service endpoint interface class, WSDL, or bus configuration) should be put   * in the same jar with the Message Driven Bean.   */
end_comment

begin_class
specifier|public
class|class
name|DispatchMDBMessageListenerImpl
implements|implements
name|MessageDrivenBean
implements|,
name|DispatchMDBMessageListener
block|{
specifier|private
specifier|static
specifier|final
name|long
name|serialVersionUID
init|=
operator|-
literal|8428728265893081763L
decl_stmt|;
comment|/**      * Looks up the target object by EJB local reference.      */
specifier|public
name|Object
name|lookupTargetObject
parameter_list|(
name|String
name|targetJndiName
parameter_list|)
throws|throws
name|Exception
block|{
name|Object
name|home
init|=
operator|new
name|InitialContext
argument_list|()
operator|.
name|lookup
argument_list|(
name|targetJndiName
argument_list|)
decl_stmt|;
name|Method
name|method
init|=
name|home
operator|.
name|getClass
argument_list|()
operator|.
name|getMethod
argument_list|(
literal|"create"
argument_list|,
operator|new
name|Class
index|[
literal|0
index|]
argument_list|)
decl_stmt|;
return|return
name|method
operator|.
name|invoke
argument_list|(
name|home
argument_list|,
operator|new
name|Object
index|[
literal|0
index|]
argument_list|)
return|;
block|}
comment|//---------------- EJB Methods
specifier|public
name|void
name|ejbCreate
parameter_list|()
block|{     }
specifier|public
name|void
name|ejbRemove
parameter_list|()
block|{     }
specifier|public
name|void
name|setMessageDrivenContext
parameter_list|(
name|MessageDrivenContext
name|mdc
parameter_list|)
block|{     }
block|}
end_class

end_unit

