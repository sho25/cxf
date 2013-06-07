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
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|IOException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|URI
import|;
end_import

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
name|Properties
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
operator|.
name|CorbaDSIServant
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
name|utils
operator|.
name|CorbaBindingHelper
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
name|utils
operator|.
name|CorbaUtils
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
name|utils
operator|.
name|OrbConfig
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
name|AddressType
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
name|PolicyType
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
name|Message
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
name|EndpointInfo
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
name|Conduit
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
name|apache
operator|.
name|cxf
operator|.
name|transport
operator|.
name|MultiplexDestination
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
name|ws
operator|.
name|addressing
operator|.
name|AttributedURIType
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
name|ws
operator|.
name|addressing
operator|.
name|EndpointReferenceType
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
name|ws
operator|.
name|addressing
operator|.
name|EndpointReferenceUtils
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
name|Policy
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
name|Current
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
name|IdAssignmentPolicyValue
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
name|IdUniquenessPolicyValue
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
name|LifespanPolicyValue
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

begin_import
import|import
name|org
operator|.
name|omg
operator|.
name|PortableServer
operator|.
name|POAHelper
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
name|POAManager
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
name|RequestProcessingPolicyValue
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
name|Servant
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
name|ThreadPolicyValue
import|;
end_import

begin_class
specifier|public
class|class
name|CorbaDestination
implements|implements
name|MultiplexDestination
block|{
specifier|private
specifier|static
specifier|final
name|String
name|IOR_SHARED_KEY
init|=
literal|"ior:shared-key"
decl_stmt|;
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
name|CorbaDestination
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|private
name|AddressType
name|address
decl_stmt|;
specifier|private
name|EndpointReferenceType
name|reference
decl_stmt|;
specifier|private
name|ORB
name|orb
decl_stmt|;
specifier|private
name|BindingInfo
name|binding
decl_stmt|;
specifier|private
name|EndpointInfo
name|endpointInfo
decl_stmt|;
specifier|private
name|OrbConfig
name|orbConfig
decl_stmt|;
specifier|private
name|MessageObserver
name|incomingObserver
decl_stmt|;
specifier|private
name|CorbaTypeMap
name|typeMap
decl_stmt|;
specifier|private
name|byte
index|[]
name|objectId
decl_stmt|;
specifier|private
name|POA
name|bindingPOA
decl_stmt|;
specifier|private
name|String
name|poaName
decl_stmt|;
specifier|private
name|String
name|serviceId
decl_stmt|;
specifier|private
name|boolean
name|isPersistent
decl_stmt|;
specifier|private
name|org
operator|.
name|omg
operator|.
name|CORBA
operator|.
name|Object
name|obj
decl_stmt|;
specifier|public
name|CorbaDestination
parameter_list|(
name|EndpointInfo
name|ei
parameter_list|,
name|OrbConfig
name|config
parameter_list|)
block|{
name|this
argument_list|(
name|ei
argument_list|,
name|config
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
specifier|public
name|CorbaDestination
parameter_list|(
name|EndpointInfo
name|ei
parameter_list|,
name|OrbConfig
name|config
parameter_list|,
name|CorbaTypeMap
name|tm
parameter_list|)
block|{
name|address
operator|=
name|ei
operator|.
name|getExtensor
argument_list|(
name|AddressType
operator|.
name|class
argument_list|)
expr_stmt|;
name|binding
operator|=
name|ei
operator|.
name|getBinding
argument_list|()
expr_stmt|;
name|reference
operator|=
operator|new
name|EndpointReferenceType
argument_list|()
expr_stmt|;
name|AttributedURIType
name|addr
init|=
operator|new
name|AttributedURIType
argument_list|()
decl_stmt|;
name|addr
operator|.
name|setValue
argument_list|(
name|address
operator|.
name|getLocation
argument_list|()
argument_list|)
expr_stmt|;
name|reference
operator|.
name|setAddress
argument_list|(
name|addr
argument_list|)
expr_stmt|;
name|endpointInfo
operator|=
name|ei
expr_stmt|;
name|orbConfig
operator|=
name|config
expr_stmt|;
if|if
condition|(
name|tm
operator|!=
literal|null
condition|)
block|{
name|typeMap
operator|=
name|tm
expr_stmt|;
block|}
else|else
block|{
name|typeMap
operator|=
name|TypeMapCache
operator|.
name|get
argument_list|(
name|binding
operator|.
name|getService
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|PolicyType
name|policy
init|=
name|ei
operator|.
name|getExtensor
argument_list|(
name|PolicyType
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|policy
operator|!=
literal|null
condition|)
block|{
name|poaName
operator|=
name|policy
operator|.
name|getPoaname
argument_list|()
expr_stmt|;
name|isPersistent
operator|=
name|policy
operator|.
name|isPersistent
argument_list|()
expr_stmt|;
name|serviceId
operator|=
name|policy
operator|.
name|getServiceid
argument_list|()
expr_stmt|;
block|}
block|}
specifier|public
name|OrbConfig
name|getOrbConfig
parameter_list|()
block|{
return|return
name|orbConfig
return|;
block|}
specifier|public
name|EndpointReferenceType
name|getAddress
parameter_list|()
block|{
return|return
name|reference
return|;
block|}
specifier|public
name|Conduit
name|getBackChannel
parameter_list|(
name|Message
name|inMessage
parameter_list|)
throws|throws
name|IOException
block|{
return|return
operator|new
name|CorbaServerConduit
argument_list|(
name|endpointInfo
argument_list|,
name|reference
argument_list|,
name|obj
argument_list|,
name|orb
argument_list|,
name|orbConfig
argument_list|,
name|typeMap
argument_list|)
return|;
block|}
specifier|public
name|BindingInfo
name|getBindingInfo
parameter_list|()
block|{
return|return
name|binding
return|;
block|}
specifier|public
name|EndpointInfo
name|getEndPointInfo
parameter_list|()
block|{
return|return
name|endpointInfo
return|;
block|}
specifier|public
name|CorbaTypeMap
name|getCorbaTypeMap
parameter_list|()
block|{
return|return
name|typeMap
return|;
block|}
specifier|public
name|void
name|shutdown
parameter_list|()
block|{
name|deactivate
argument_list|()
expr_stmt|;
if|if
condition|(
name|orb
operator|!=
literal|null
condition|)
block|{
try|try
block|{
comment|// Ask for the ORB to be destroyed.  If another destination is using it, we'll
comment|// simply decrement a use count, but not destroy the ORB so that we don't break the
comment|// other CorbaDestination.
if|if
condition|(
name|CorbaUtils
operator|.
name|isIOR
argument_list|(
name|getDestinationAddress
argument_list|()
argument_list|)
condition|)
block|{
name|CorbaBindingHelper
operator|.
name|destroyORB
argument_list|(
name|IOR_SHARED_KEY
argument_list|,
name|orb
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|CorbaBindingHelper
operator|.
name|destroyORB
argument_list|(
name|getDestinationAddress
argument_list|()
argument_list|,
name|orb
argument_list|)
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|Exception
name|ex
parameter_list|)
block|{
throw|throw
operator|new
name|CorbaBindingException
argument_list|(
name|ex
argument_list|)
throw|;
block|}
name|orb
operator|=
literal|null
expr_stmt|;
block|}
block|}
specifier|public
name|ORB
name|getORB
parameter_list|(
name|List
argument_list|<
name|String
argument_list|>
name|orbArgs
parameter_list|,
name|String
name|location
parameter_list|,
name|java
operator|.
name|util
operator|.
name|Properties
name|props
parameter_list|)
block|{
comment|// See if an ORB has already been created for the given address. If so,
comment|// we'll simply use it
comment|// so that we don't try re-create another ORB on the same host and port.
if|if
condition|(
name|CorbaUtils
operator|.
name|isIOR
argument_list|(
name|location
argument_list|)
condition|)
block|{
name|location
operator|=
name|IOR_SHARED_KEY
expr_stmt|;
block|}
name|orb
operator|=
name|CorbaBindingHelper
operator|.
name|getAddressSpecificORB
argument_list|(
name|location
argument_list|,
name|props
argument_list|,
name|orbArgs
argument_list|)
expr_stmt|;
comment|// Get the binding helper to remember that we need this ORB kept alive, even if another
comment|// destination tries to destroy it.
name|CorbaBindingHelper
operator|.
name|keepORBAlive
argument_list|(
name|location
argument_list|)
expr_stmt|;
return|return
name|orb
return|;
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
specifier|protected
name|AddressType
name|getAddressType
parameter_list|()
block|{
return|return
name|address
return|;
block|}
specifier|public
specifier|synchronized
name|void
name|setMessageObserver
parameter_list|(
name|MessageObserver
name|observer
parameter_list|)
block|{
if|if
condition|(
name|observer
operator|!=
name|incomingObserver
condition|)
block|{
name|MessageObserver
name|old
init|=
name|incomingObserver
decl_stmt|;
name|incomingObserver
operator|=
name|observer
expr_stmt|;
if|if
condition|(
name|observer
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|old
operator|==
literal|null
condition|)
block|{
name|activate
argument_list|()
expr_stmt|;
block|}
block|}
else|else
block|{
if|if
condition|(
name|old
operator|!=
literal|null
condition|)
block|{
name|deactivate
argument_list|()
expr_stmt|;
block|}
block|}
block|}
block|}
specifier|public
name|void
name|activate
parameter_list|()
block|{
name|java
operator|.
name|util
operator|.
name|Properties
name|props
init|=
operator|new
name|java
operator|.
name|util
operator|.
name|Properties
argument_list|()
decl_stmt|;
name|Properties
name|configSpecifiedOrbProperties
init|=
name|orbConfig
operator|.
name|getOrbProperties
argument_list|()
decl_stmt|;
name|props
operator|.
name|putAll
argument_list|(
name|configSpecifiedOrbProperties
argument_list|)
expr_stmt|;
if|if
condition|(
name|orbConfig
operator|.
name|getOrbClass
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|props
operator|.
name|put
argument_list|(
literal|"org.omg.CORBA.ORBClass"
argument_list|,
name|orbConfig
operator|.
name|getOrbClass
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|orbConfig
operator|.
name|getOrbSingletonClass
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|props
operator|.
name|put
argument_list|(
literal|"org.omg.CORBA.ORBSingletonClass"
argument_list|,
name|orbConfig
operator|.
name|getOrbSingletonClass
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|String
name|location
init|=
name|getDestinationAddress
argument_list|()
decl_stmt|;
if|if
condition|(
operator|!
name|CorbaUtils
operator|.
name|isValidURL
argument_list|(
name|location
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|CorbaBindingException
argument_list|(
literal|"Invalid addressing specified for CORBA port location"
argument_list|)
throw|;
block|}
name|LOG
operator|.
name|info
argument_list|(
literal|"Service address retrieved: "
operator|+
name|location
argument_list|)
expr_stmt|;
name|URI
name|addressURI
init|=
literal|null
decl_stmt|;
try|try
block|{
name|addressURI
operator|=
operator|new
name|URI
argument_list|(
name|location
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|java
operator|.
name|net
operator|.
name|URISyntaxException
name|ex
parameter_list|)
block|{
throw|throw
operator|new
name|CorbaBindingException
argument_list|(
literal|"Unable to create ORB with address "
operator|+
name|address
argument_list|)
throw|;
block|}
name|List
argument_list|<
name|String
argument_list|>
name|orbArgs
init|=
operator|new
name|ArrayList
argument_list|<
name|String
argument_list|>
argument_list|(
name|orbConfig
operator|.
name|getOrbArgs
argument_list|()
argument_list|)
decl_stmt|;
name|String
name|scheme
init|=
name|addressURI
operator|.
name|getScheme
argument_list|()
decl_stmt|;
comment|// A corbaloc address gives us host and port information to use when
comment|// setting up the
comment|// endpoint for the ORB. Other types of references will just create ORBs
comment|// on the
comment|// host and port used when no preference has been specified.
if|if
condition|(
name|poaName
operator|!=
literal|null
condition|)
block|{
name|poaName
operator|=
name|poaName
operator|.
name|replace
argument_list|(
literal|'.'
argument_list|,
literal|'_'
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
literal|"corbaloc"
operator|.
name|equals
argument_list|(
name|scheme
argument_list|)
condition|)
block|{
if|if
condition|(
name|poaName
operator|==
literal|null
condition|)
block|{
name|poaName
operator|=
name|getEndPointInfo
argument_list|()
operator|.
name|getName
argument_list|()
operator|.
name|getLocalPart
argument_list|()
operator|.
name|replace
argument_list|(
literal|'.'
argument_list|,
literal|'_'
argument_list|)
expr_stmt|;
block|}
name|setCorbaLocArgs
argument_list|(
name|addressURI
argument_list|,
name|orbArgs
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
literal|"corbaname"
operator|.
name|equals
argument_list|(
name|scheme
argument_list|)
condition|)
block|{
name|int
name|idx
init|=
name|location
operator|.
name|indexOf
argument_list|(
literal|"#"
argument_list|)
decl_stmt|;
if|if
condition|(
name|idx
operator|!=
operator|-
literal|1
condition|)
block|{
name|serviceId
operator|=
name|location
operator|.
name|substring
argument_list|(
name|idx
operator|+
literal|1
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
name|isPersistent
condition|)
block|{
if|if
condition|(
name|poaName
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|CorbaBindingException
argument_list|(
literal|"POA name missing for corba port "
operator|+
literal|"with a persistent policy"
argument_list|)
throw|;
block|}
block|}
else|else
block|{
name|poaName
operator|=
name|CorbaUtils
operator|.
name|getUniquePOAName
argument_list|(
name|getEndPointInfo
argument_list|()
operator|.
name|getService
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|,
name|getEndPointInfo
argument_list|()
operator|.
name|getName
argument_list|()
operator|.
name|getLocalPart
argument_list|()
argument_list|,
name|poaName
argument_list|)
operator|.
name|replace
argument_list|(
literal|'.'
argument_list|,
literal|'_'
argument_list|)
expr_stmt|;
block|}
name|orb
operator|=
name|getORB
argument_list|(
name|orbArgs
argument_list|,
name|location
argument_list|,
name|props
argument_list|)
expr_stmt|;
try|try
block|{
name|POA
name|rootPOA
init|=
name|POAHelper
operator|.
name|narrow
argument_list|(
name|orb
operator|.
name|resolve_initial_references
argument_list|(
literal|"RootPOA"
argument_list|)
argument_list|)
decl_stmt|;
name|POAManager
name|poaManager
init|=
name|rootPOA
operator|.
name|the_POAManager
argument_list|()
decl_stmt|;
try|try
block|{
name|bindingPOA
operator|=
name|rootPOA
operator|.
name|find_POA
argument_list|(
name|poaName
argument_list|,
literal|false
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|org
operator|.
name|omg
operator|.
name|PortableServer
operator|.
name|POAPackage
operator|.
name|AdapterNonExistent
name|ex
parameter_list|)
block|{
comment|// do nothing
block|}
comment|// When using object references, we can run into a situation where
comment|// we are implementing
comment|// multiple instances of the same port type such that we would end
comment|// up using the same
comment|// poaname for each when persistance is used. Handle this case by
comment|// not throwing an
comment|// exception at this point during the activation, we should see an
comment|// exception if we try
comment|// an activate two objects with the same servant ID instead.
if|if
condition|(
name|bindingPOA
operator|!=
literal|null
operator|&&
operator|!
name|isPersistent
operator|&&
name|serviceId
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|CorbaBindingException
argument_list|(
literal|"Corba Port activation failed because the poa "
operator|+
name|poaName
operator|+
literal|" already exists"
argument_list|)
throw|;
block|}
elseif|else
if|if
condition|(
name|bindingPOA
operator|==
literal|null
condition|)
block|{
name|bindingPOA
operator|=
name|createPOA
argument_list|(
name|poaName
argument_list|,
name|rootPOA
argument_list|,
name|poaManager
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|bindingPOA
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|CorbaBindingException
argument_list|(
literal|"Unable to create CXF CORBA Binding POA"
argument_list|)
throw|;
block|}
name|CorbaDSIServant
name|servant
init|=
operator|new
name|CorbaDSIServant
argument_list|()
decl_stmt|;
name|servant
operator|.
name|init
argument_list|(
name|orb
argument_list|,
name|bindingPOA
argument_list|,
name|this
argument_list|,
name|incomingObserver
argument_list|,
name|typeMap
argument_list|)
expr_stmt|;
if|if
condition|(
name|serviceId
operator|!=
literal|null
condition|)
block|{
name|objectId
operator|=
name|serviceId
operator|.
name|getBytes
argument_list|()
expr_stmt|;
try|try
block|{
name|bindingPOA
operator|.
name|activate_object_with_id
argument_list|(
name|objectId
argument_list|,
name|servant
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|org
operator|.
name|omg
operator|.
name|PortableServer
operator|.
name|POAPackage
operator|.
name|ObjectAlreadyActive
name|ex
parameter_list|)
block|{
if|if
condition|(
operator|!
name|isPersistent
condition|)
block|{
throw|throw
operator|new
name|CorbaBindingException
argument_list|(
literal|"Object "
operator|+
name|serviceId
operator|+
literal|" already active for non-persistent poa"
argument_list|)
throw|;
block|}
block|}
block|}
else|else
block|{
name|objectId
operator|=
name|bindingPOA
operator|.
name|activate_object
argument_list|(
name|servant
argument_list|)
expr_stmt|;
block|}
name|bindingPOA
operator|.
name|set_servant
argument_list|(
name|servant
argument_list|)
expr_stmt|;
name|obj
operator|=
name|bindingPOA
operator|.
name|id_to_reference
argument_list|(
name|objectId
argument_list|)
expr_stmt|;
name|orbConfig
operator|.
name|exportObjectReference
argument_list|(
name|orb
argument_list|,
name|obj
argument_list|,
name|location
argument_list|,
name|address
argument_list|)
expr_stmt|;
name|populateEpr
argument_list|(
name|orb
operator|.
name|object_to_string
argument_list|(
name|obj
argument_list|)
argument_list|)
expr_stmt|;
name|LOG
operator|.
name|info
argument_list|(
literal|"Object Reference: "
operator|+
name|orb
operator|.
name|object_to_string
argument_list|(
name|obj
argument_list|)
argument_list|)
expr_stmt|;
comment|// TODO: Provide other export mechanisms?
name|poaManager
operator|.
name|activate
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|ex
parameter_list|)
block|{
throw|throw
operator|new
name|CorbaBindingException
argument_list|(
literal|"Unable to activate CORBA servant"
argument_list|,
name|ex
argument_list|)
throw|;
block|}
block|}
specifier|private
name|void
name|populateEpr
parameter_list|(
name|String
name|ior
parameter_list|)
block|{
name|AttributedURIType
name|addr
init|=
operator|new
name|AttributedURIType
argument_list|()
decl_stmt|;
name|addr
operator|.
name|setValue
argument_list|(
name|ior
argument_list|)
expr_stmt|;
name|reference
operator|.
name|setAddress
argument_list|(
name|addr
argument_list|)
expr_stmt|;
block|}
specifier|public
name|String
name|getDestinationAddress
parameter_list|()
block|{
comment|// We should check the endpoint first for an address.  This allows object references
comment|// to use the address that is associated with their endpoint instead of the single
comment|// address for a particular port type that is listed in the wsdl.  Otherwise, for all
comment|// object references we want to create, we would need to add the address to the wsdl
comment|// file before running the application.
name|String
name|location
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|endpointInfo
operator|!=
literal|null
condition|)
block|{
name|location
operator|=
name|endpointInfo
operator|.
name|getAddress
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|location
operator|==
literal|null
condition|)
block|{
name|location
operator|=
name|address
operator|.
name|getLocation
argument_list|()
expr_stmt|;
block|}
return|return
name|location
return|;
block|}
specifier|public
name|MessageObserver
name|getMessageObserver
parameter_list|()
block|{
return|return
name|incomingObserver
return|;
block|}
specifier|public
name|void
name|deactivate
parameter_list|()
block|{
if|if
condition|(
name|orb
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|bindingPOA
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|CorbaBindingException
argument_list|(
literal|"Corba Port deactivation failed because the poa is null"
argument_list|)
throw|;
block|}
try|try
block|{
name|bindingPOA
operator|.
name|deactivate_object
argument_list|(
name|objectId
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|ex
parameter_list|)
block|{
throw|throw
operator|new
name|CorbaBindingException
argument_list|(
literal|"Unable to deactivate CORBA servant"
argument_list|,
name|ex
argument_list|)
throw|;
block|}
block|}
block|}
specifier|private
name|void
name|setCorbaLocArgs
parameter_list|(
name|URI
name|addressURI
parameter_list|,
name|List
argument_list|<
name|String
argument_list|>
name|orbArgs
parameter_list|)
block|{
name|String
name|schemeSpecificPart
init|=
name|addressURI
operator|.
name|getSchemeSpecificPart
argument_list|()
decl_stmt|;
name|int
name|keyIndex
init|=
name|schemeSpecificPart
operator|.
name|indexOf
argument_list|(
literal|'/'
argument_list|)
decl_stmt|;
name|String
name|corbaAddr
init|=
name|schemeSpecificPart
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|keyIndex
argument_list|)
decl_stmt|;
name|String
name|key
init|=
name|schemeSpecificPart
operator|.
name|substring
argument_list|(
name|keyIndex
operator|+
literal|1
argument_list|)
decl_stmt|;
name|int
name|index
init|=
name|corbaAddr
operator|.
name|indexOf
argument_list|(
literal|':'
argument_list|)
decl_stmt|;
name|String
name|protocol
init|=
literal|"iiop"
decl_stmt|;
if|if
condition|(
name|index
operator|!=
literal|0
condition|)
block|{
name|protocol
operator|=
name|corbaAddr
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|index
argument_list|)
expr_stmt|;
block|}
name|int
name|oldIndex
init|=
name|index
decl_stmt|;
name|index
operator|=
name|corbaAddr
operator|.
name|indexOf
argument_list|(
literal|':'
argument_list|,
name|oldIndex
operator|+
literal|1
argument_list|)
expr_stmt|;
name|String
name|host
init|=
name|corbaAddr
operator|.
name|substring
argument_list|(
name|oldIndex
operator|+
literal|1
argument_list|,
name|index
argument_list|)
decl_stmt|;
name|String
name|port
init|=
name|corbaAddr
operator|.
name|substring
argument_list|(
name|index
operator|+
literal|1
argument_list|)
decl_stmt|;
name|orbArgs
operator|.
name|add
argument_list|(
literal|"-ORB"
operator|+
name|key
operator|+
literal|":"
operator|+
name|protocol
operator|+
literal|":host"
argument_list|)
expr_stmt|;
name|orbArgs
operator|.
name|add
argument_list|(
name|host
argument_list|)
expr_stmt|;
name|orbArgs
operator|.
name|add
argument_list|(
literal|"-ORB"
operator|+
name|key
operator|+
literal|":"
operator|+
name|protocol
operator|+
literal|":port"
argument_list|)
expr_stmt|;
name|orbArgs
operator|.
name|add
argument_list|(
name|port
argument_list|)
expr_stmt|;
name|orbArgs
operator|.
name|add
argument_list|(
literal|"-ORBpoa:"
operator|+
name|poaName
operator|+
literal|":direct_persistent"
argument_list|)
expr_stmt|;
name|orbArgs
operator|.
name|add
argument_list|(
literal|"true"
argument_list|)
expr_stmt|;
name|orbArgs
operator|.
name|add
argument_list|(
literal|"-ORBpoa:"
operator|+
name|poaName
operator|+
literal|":well_known_address"
argument_list|)
expr_stmt|;
name|orbArgs
operator|.
name|add
argument_list|(
name|key
argument_list|)
expr_stmt|;
name|isPersistent
operator|=
literal|true
expr_stmt|;
name|serviceId
operator|=
name|key
expr_stmt|;
block|}
specifier|protected
name|POA
name|createPOA
parameter_list|(
name|String
name|name
parameter_list|,
name|POA
name|parentPOA
parameter_list|,
name|POAManager
name|poaManager
parameter_list|)
block|{
name|List
argument_list|<
name|Policy
argument_list|>
name|policies
init|=
operator|new
name|ArrayList
argument_list|<
name|Policy
argument_list|>
argument_list|()
decl_stmt|;
name|policies
operator|.
name|add
argument_list|(
name|parentPOA
operator|.
name|create_thread_policy
argument_list|(
name|ThreadPolicyValue
operator|.
name|ORB_CTRL_MODEL
argument_list|)
argument_list|)
expr_stmt|;
if|if
condition|(
name|isPersistent
condition|)
block|{
name|policies
operator|.
name|add
argument_list|(
name|parentPOA
operator|.
name|create_lifespan_policy
argument_list|(
name|LifespanPolicyValue
operator|.
name|PERSISTENT
argument_list|)
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|policies
operator|.
name|add
argument_list|(
name|parentPOA
operator|.
name|create_lifespan_policy
argument_list|(
name|LifespanPolicyValue
operator|.
name|TRANSIENT
argument_list|)
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|serviceId
operator|!=
literal|null
condition|)
block|{
name|policies
operator|.
name|add
argument_list|(
name|parentPOA
operator|.
name|create_id_assignment_policy
argument_list|(
name|IdAssignmentPolicyValue
operator|.
name|USER_ID
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|policies
operator|.
name|add
argument_list|(
name|parentPOA
operator|.
name|create_id_uniqueness_policy
argument_list|(
name|IdUniquenessPolicyValue
operator|.
name|MULTIPLE_ID
argument_list|)
argument_list|)
expr_stmt|;
name|RequestProcessingPolicyValue
name|value
init|=
name|RequestProcessingPolicyValue
operator|.
name|USE_DEFAULT_SERVANT
decl_stmt|;
name|policies
operator|.
name|add
argument_list|(
name|parentPOA
operator|.
name|create_request_processing_policy
argument_list|(
name|value
argument_list|)
argument_list|)
expr_stmt|;
name|orbConfig
operator|.
name|addPOAPolicies
argument_list|(
name|orb
argument_list|,
name|name
argument_list|,
name|parentPOA
argument_list|,
name|poaManager
argument_list|,
name|policies
argument_list|)
expr_stmt|;
name|Policy
index|[]
name|policyList
init|=
name|policies
operator|.
name|toArray
argument_list|(
operator|new
name|Policy
index|[
name|policies
operator|.
name|size
argument_list|()
index|]
argument_list|)
decl_stmt|;
try|try
block|{
return|return
name|parentPOA
operator|.
name|create_POA
argument_list|(
name|name
argument_list|,
name|poaManager
argument_list|,
name|policyList
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|Exception
name|ex
parameter_list|)
block|{
throw|throw
operator|new
name|CorbaBindingException
argument_list|(
literal|"Could not create POA during activation"
argument_list|,
name|ex
argument_list|)
throw|;
block|}
block|}
specifier|public
name|EndpointReferenceType
name|getAddressWithId
parameter_list|(
name|String
name|id
parameter_list|)
block|{
name|EndpointReferenceType
name|ref
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|bindingPOA
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|CorbaBindingException
argument_list|(
literal|"getAddressWithId failed because the poa is null"
argument_list|)
throw|;
block|}
try|try
block|{
name|Servant
name|servant
init|=
name|bindingPOA
operator|.
name|id_to_servant
argument_list|(
name|objectId
argument_list|)
decl_stmt|;
name|org
operator|.
name|omg
operator|.
name|CORBA
operator|.
name|Object
name|objRef
init|=
name|bindingPOA
operator|.
name|create_reference_with_id
argument_list|(
name|id
operator|.
name|getBytes
argument_list|()
argument_list|,
name|servant
operator|.
name|_all_interfaces
argument_list|(
name|bindingPOA
argument_list|,
name|objectId
argument_list|)
index|[
literal|0
index|]
argument_list|)
decl_stmt|;
name|AddressType
name|addr
init|=
operator|new
name|AddressType
argument_list|()
decl_stmt|;
name|orbConfig
operator|.
name|exportObjectReference
argument_list|(
name|orb
argument_list|,
name|objRef
argument_list|,
name|address
operator|.
name|getLocation
argument_list|()
argument_list|,
name|addr
argument_list|)
expr_stmt|;
name|ref
operator|=
name|EndpointReferenceUtils
operator|.
name|getEndpointReference
argument_list|(
name|addr
operator|.
name|getLocation
argument_list|()
argument_list|)
expr_stmt|;
name|EndpointInfo
name|ei
init|=
name|getEndPointInfo
argument_list|()
decl_stmt|;
if|if
condition|(
name|ei
operator|.
name|getService
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|EndpointReferenceUtils
operator|.
name|setServiceAndPortName
argument_list|(
name|ref
argument_list|,
name|ei
operator|.
name|getService
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|,
name|ei
operator|.
name|getName
argument_list|()
operator|.
name|getLocalPart
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|CorbaBindingException
argument_list|(
literal|"Failed to getAddressWithId, reason:"
operator|+
name|e
operator|.
name|toString
argument_list|()
argument_list|,
name|e
argument_list|)
throw|;
block|}
return|return
name|ref
return|;
block|}
specifier|public
name|String
name|getId
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|contextMap
parameter_list|)
block|{
name|String
name|id
init|=
literal|null
decl_stmt|;
try|try
block|{
name|Current
name|currentPoa
init|=
operator|(
name|Current
operator|)
name|orb
operator|.
name|resolve_initial_references
argument_list|(
literal|"POACurrent"
argument_list|)
decl_stmt|;
name|byte
index|[]
name|idBytes
init|=
name|currentPoa
operator|.
name|get_object_id
argument_list|()
decl_stmt|;
name|id
operator|=
operator|new
name|String
argument_list|(
name|idBytes
argument_list|)
expr_stmt|;
comment|//NOPMD
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|CorbaBindingException
argument_list|(
literal|"Unable to getId, current is unavailable, reason: "
operator|+
name|e
argument_list|,
name|e
argument_list|)
throw|;
block|}
return|return
name|id
return|;
block|}
block|}
end_class

end_unit

