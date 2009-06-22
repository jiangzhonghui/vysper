/*
 *  Licensed to the Apache Software Foundation (ASF) under one
 *  or more contributor license agreements.  See the NOTICE file
 *  distributed with this work for additional information
 *  regarding copyright ownership.  The ASF licenses this file
 *  to you under the Apache License, Version 2.0 (the
 *  "License"); you may not use this file except in compliance
 *  with the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing,
 *  software distributed under the License is distributed on an
 *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *  KIND, either express or implied.  See the License for the
 *  specific language governing permissions and limitations
 *  under the License.
 *
 */
package org.apache.vysper.xmpp.modules.extension.xep0060_pubsub;

import org.apache.vysper.compliance.SpecCompliant;
import org.apache.vysper.xmpp.modules.core.base.handler.DefaultIQHandler;
import org.apache.vysper.xmpp.modules.extension.xep0060_pubsub.model.CollectionNode;
import org.apache.vysper.xmpp.stanza.Stanza;
import org.apache.vysper.xmpp.uuid.JVMBuiltinUUIDGenerator;
import org.apache.vysper.xmpp.uuid.UUIDGenerator;
import org.apache.vysper.xmpp.xmlfragment.XMLElement;

/**
 * This abstract class is the superclass of all pubsub (and related) handlers. It is responsible for correctly verifying whether a
 * given Stanza is to be handled, or not.
 *
 * @author The Apache MINA Project (http://mina.apache.org)
 */
@SpecCompliant(spec="xep-0060", status= SpecCompliant.ComplianceStatus.IN_PROGRESS, coverage = SpecCompliant.ComplianceCoverage.UNSUPPORTED)
public abstract class AbstractPublishSubscribeIQHandler extends DefaultIQHandler {

    // the pubsub service itself is a collection node
    protected CollectionNode root;
    // we need to generate some IDs
    protected UUIDGenerator idGenerator;

    /**
     * Initialize the handler with the given root CollectionNode.
     * 
     * @param root the one and only "root" CollectionNode
     */
    public AbstractPublishSubscribeIQHandler(CollectionNode root) {
        this.root = root;
        this.idGenerator = new JVMBuiltinUUIDGenerator();
    }

    /**
     * Delegate the verification to DefaultIQHandler#verifyInnerNamespace(Stanza, String).
     * 
     * @see DefaultIQHandler#verifyNamespace(Stanza)
     * @return true if the namespace of the inner element matches the pubsub namespace.
     */
    @Override
    protected boolean verifyNamespace(Stanza stanza) {
        return verifyInnerNamespace(stanza, getNamespace());
    }

    /**
     * Checks whether the inner element of the stanza is a "pubsub" element and if the
     * worker element (the element within the pubsub element) matches the one of the current class.
     * 
     * @param stanza the Stanza to be checked.
     * @return true if this class is responsible for handling the stanza.
     */
    @Override
    protected boolean verifyInnerElement(Stanza stanza) {
        return verifyInnerElementWorker(stanza, "pubsub")
        && verifySingleElementParameter(stanza.getFirstInnerElement(), getWorkerElement());
    }

    /**
     * Verifies if there is only one inner element available and if it matches the element name given.
     * @param pubsub the XMLElement to check
     * @param element the name of the expected inner element.
     * @return true if the name matches the only available inner element.
     */
    protected boolean verifySingleElementParameter(XMLElement pubsub, String element) {
        return pubsub.getVerifier().subElementsPresentExact(1)
        && pubsub.getVerifier().subElementPresent(element);
    }

    /**
     * Implement this method to specify which namespace the pubsub element in the stanza should lie in.
     * @return the namespace this class is expecting.
     */
    protected abstract String getNamespace();
    
    /**
     * Implement this method to specify which inner element of pubsub element this class expects.
     * @return the name of the inner element this class is expecting.
     */
    protected abstract String getWorkerElement();
}
