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
name|binding
operator|.
name|corba
operator|.
name|runtime
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|ArrayList
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|HashMap
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Iterator
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|List
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Map
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|logging
operator|.
name|Level
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|logging
operator|.
name|Logger
import|;
end_import

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

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|binding
operator|.
name|corba
operator|.
name|CorbaBindingException
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|binding
operator|.
name|corba
operator|.
name|CorbaDestination
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|binding
operator|.
name|corba
operator|.
name|CorbaMessage
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|binding
operator|.
name|corba
operator|.
name|CorbaTypeMap
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|binding
operator|.
name|corba
operator|.
name|wsdl
operator|.
name|BindingType
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|binding
operator|.
name|corba
operator|.
name|wsdl
operator|.
name|OperationType
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|common
operator|.
name|logging
operator|.
name|LogUtils
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|message
operator|.
name|Exchange
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|message
operator|.
name|ExchangeImpl
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|message
operator|.
name|MessageImpl
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|service
operator|.
name|model
operator|.
name|BindingInfo
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|service
operator|.
name|model
operator|.
name|BindingOperationInfo
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|transport
operator|.
name|MessageObserver
import|;
end_import

begin_import
import|import
name|org
operator|.
name|omg
operator|.
name|CORBA
operator|.
name|ORB
import|;
end_import

begin_import
import|import
name|org
operator|.
name|omg
operator|.
name|CORBA
operator|.
name|ServerRequest
import|;
end_import

begin_import
import|import
name|org
operator|.
name|omg
operator|.
name|PortableServer
operator|.
name|DynamicImplementation
import|;
end_import

begin_import
import|import
name|org
operator|.
name|omg
operator|.
name|PortableServer
operator|.
name|POA
import|;
end_import

begin_class
specifier|public
class|class
name|CorbaDSIServant
extends|extends
name|DynamicImplementation
block|{
specifier|private
specifier|static
specifier|final
name|Logger
name|LOG
init|=
name|LogUtils
operator|.
name|getL7dLogger
argument_list|(
name|CorbaDSIServant
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|private
name|ORB
name|orb
decl_stmt|;
specifier|private
name|POA
name|servantPOA
decl_stmt|;
specifier|private
name|List
argument_list|<
name|String
argument_list|>
name|interfaces
decl_stmt|;
specifier|private
name|MessageObserver
name|incomingObserver
decl_stmt|;
specifier|private
name|CorbaDestination
name|destination
decl_stmt|;
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|QName
argument_list|>
name|operationMap
decl_stmt|;
specifier|private
name|CorbaTypeMap
name|typeMap
decl_stmt|;
specifier|public
name|CorbaDSIServant
parameter_list|()
block|{
comment|//Complete
block|}
specifier|public
name|void
name|init
parameter_list|(
name|ORB
name|theOrb
parameter_list|,
name|POA
name|poa
parameter_list|,
name|CorbaDestination
name|dest
parameter_list|,
name|MessageObserver
name|observer
parameter_list|)
block|{
name|init
argument_list|(
name|theOrb
argument_list|,
name|poa
argument_list|,
name|dest
argument_list|,
name|observer
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|init
parameter_list|(
name|ORB
name|theOrb
parameter_list|,
name|POA
name|poa
parameter_list|,
name|CorbaDestination
name|dest
parameter_list|,
name|MessageObserver
name|observer
parameter_list|,
name|CorbaTypeMap
name|map
parameter_list|)
block|{
name|orb
operator|=
name|theOrb
expr_stmt|;
name|servantPOA
operator|=
name|poa
expr_stmt|;
name|destination
operator|=
name|dest
expr_stmt|;
name|incomingObserver
operator|=
name|observer
expr_stmt|;
name|typeMap
operator|=
name|map
expr_stmt|;
comment|// Get the list of interfaces that this servant will support
try|try
block|{
name|BindingType
name|bindType
init|=
name|destination
operator|.
name|getBindingInfo
argument_list|()
operator|.
name|getExtensor
argument_list|(
name|BindingType
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|bindType
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|CorbaBindingException
argument_list|(
literal|"Unable to determine corba binding information"
argument_list|)
throw|;
block|}
name|List
argument_list|<
name|String
argument_list|>
name|bases
init|=
name|bindType
operator|.
name|getBases
argument_list|()
decl_stmt|;
name|interfaces
operator|=
operator|new
name|ArrayList
argument_list|<
name|String
argument_list|>
argument_list|()
expr_stmt|;
name|interfaces
operator|.
name|add
argument_list|(
name|bindType
operator|.
name|getRepositoryID
argument_list|()
argument_list|)
expr_stmt|;
for|for
control|(
name|Iterator
argument_list|<
name|String
argument_list|>
name|iter
init|=
name|bases
operator|.
name|iterator
argument_list|()
init|;
name|iter
operator|.
name|hasNext
argument_list|()
condition|;
control|)
block|{
name|interfaces
operator|.
name|add
argument_list|(
name|iter
operator|.
name|next
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|java
operator|.
name|lang
operator|.
name|Exception
name|ex
parameter_list|)
block|{
name|LOG
operator|.
name|log
argument_list|(
name|Level
operator|.
name|SEVERE
argument_list|,
literal|"Couldn't initialize the corba DSI servant"
argument_list|)
expr_stmt|;
throw|throw
operator|new
name|CorbaBindingException
argument_list|(
name|ex
argument_list|)
throw|;
block|}
comment|// Build the list of CORBA operations and the WSDL operations they map to.  Note that
comment|// the WSDL operation name may not always match the CORBA operation name.
name|BindingInfo
name|bInfo
init|=
name|destination
operator|.
name|getBindingInfo
argument_list|()
decl_stmt|;
name|Iterator
name|i
init|=
name|bInfo
operator|.
name|getOperations
argument_list|()
operator|.
name|iterator
argument_list|()
decl_stmt|;
name|operationMap
operator|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|QName
argument_list|>
argument_list|(
name|bInfo
operator|.
name|getOperations
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
while|while
condition|(
name|i
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|BindingOperationInfo
name|bopInfo
init|=
operator|(
name|BindingOperationInfo
operator|)
name|i
operator|.
name|next
argument_list|()
decl_stmt|;
name|OperationType
name|opType
init|=
name|bopInfo
operator|.
name|getExtensor
argument_list|(
name|OperationType
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|opType
operator|!=
literal|null
condition|)
block|{
name|operationMap
operator|.
name|put
argument_list|(
name|opType
operator|.
name|getName
argument_list|()
argument_list|,
name|bopInfo
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
specifier|protected
name|MessageObserver
name|getObserver
parameter_list|()
block|{
return|return
name|incomingObserver
return|;
block|}
specifier|protected
name|void
name|setObserver
parameter_list|(
name|MessageObserver
name|observer
parameter_list|)
block|{
name|incomingObserver
operator|=
name|observer
expr_stmt|;
block|}
specifier|protected
name|ORB
name|getOrb
parameter_list|()
block|{
return|return
name|orb
return|;
block|}
specifier|public
name|CorbaDestination
name|getDestination
parameter_list|()
block|{
return|return
name|destination
return|;
block|}
specifier|public
name|Map
argument_list|<
name|String
argument_list|,
name|QName
argument_list|>
name|getOperationMapping
parameter_list|()
block|{
return|return
name|operationMap
return|;
block|}
specifier|public
name|void
name|setOperationMapping
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|QName
argument_list|>
name|map
parameter_list|)
block|{
name|operationMap
operator|=
name|map
expr_stmt|;
block|}
specifier|public
name|void
name|setCorbaTypeMap
parameter_list|(
name|CorbaTypeMap
name|map
parameter_list|)
block|{
name|typeMap
operator|=
name|map
expr_stmt|;
block|}
specifier|public
name|void
name|invoke
parameter_list|(
name|ServerRequest
name|request
parameter_list|)
throws|throws
name|CorbaBindingException
block|{
name|MessageImpl
name|msgImpl
init|=
operator|new
name|MessageImpl
argument_list|()
decl_stmt|;
name|msgImpl
operator|.
name|setDestination
argument_list|(
name|getDestination
argument_list|()
argument_list|)
expr_stmt|;
name|Exchange
name|exg
init|=
operator|new
name|ExchangeImpl
argument_list|()
decl_stmt|;
name|exg
operator|.
name|put
argument_list|(
name|String
operator|.
name|class
argument_list|,
name|operationMap
operator|.
name|get
argument_list|(
name|request
operator|.
name|operation
argument_list|()
argument_list|)
operator|.
name|getLocalPart
argument_list|()
argument_list|)
expr_stmt|;
name|exg
operator|.
name|put
argument_list|(
name|ORB
operator|.
name|class
argument_list|,
name|getOrb
argument_list|()
argument_list|)
expr_stmt|;
name|exg
operator|.
name|put
argument_list|(
name|ServerRequest
operator|.
name|class
argument_list|,
name|request
argument_list|)
expr_stmt|;
name|msgImpl
operator|.
name|setExchange
argument_list|(
name|exg
argument_list|)
expr_stmt|;
name|CorbaMessage
name|msg
init|=
operator|new
name|CorbaMessage
argument_list|(
name|msgImpl
argument_list|)
decl_stmt|;
name|msg
operator|.
name|setCorbaTypeMap
argument_list|(
name|typeMap
argument_list|)
expr_stmt|;
comment|// invokes the interceptors
name|getObserver
argument_list|()
operator|.
name|onMessage
argument_list|(
name|msg
argument_list|)
expr_stmt|;
block|}
specifier|public
name|String
index|[]
name|_all_interfaces
parameter_list|(
name|POA
name|poa
parameter_list|,
name|byte
index|[]
name|objectId
parameter_list|)
block|{
return|return
name|interfaces
operator|.
name|toArray
argument_list|(
operator|new
name|String
index|[
name|interfaces
operator|.
name|size
argument_list|()
index|]
argument_list|)
return|;
block|}
specifier|public
name|POA
name|_default_POA
parameter_list|()
block|{
return|return
name|servantPOA
return|;
block|}
block|}
end_class

end_unit

