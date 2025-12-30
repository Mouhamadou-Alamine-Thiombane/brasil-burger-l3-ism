<?php

namespace App\Form;

use Symfony\Component\Form\AbstractType;
use Symfony\Component\Form\FormBuilderInterface;
use Symfony\Component\OptionsResolver\OptionsResolver;
use Symfony\Component\Form\Extension\Core\Type\TextType;
use Symfony\Component\Form\Extension\Core\Type\DateType;
use Symfony\Component\Form\Extension\Core\Type\ChoiceType;
use Symfony\Component\Form\Extension\Core\Type\SubmitType;

class CommandeFilterType extends AbstractType
{
    public function buildForm(FormBuilderInterface $builder, array $options): void
    {
        $builder
            ->add('numero', TextType::class, [
                'required' => false,
                'label' => 'Numéro de commande',
                'attr' => ['placeholder' => 'Rechercher par numéro']
            ])
            ->add('client', TextType::class, [
                'required' => false,
                'label' => 'Nom du client',
                'attr' => ['placeholder' => 'Nom ou prénom']
            ])
            ->add('telephone', TextType::class, [
                'required' => false,
                'label' => 'Téléphone',
                'attr' => ['placeholder' => '77 123 45 67']
            ])
            ->add('dateDebut', DateType::class, [
                'required' => false,
                'label' => 'Date début',
                'widget' => 'single_text',
            ])
            ->add('dateFin', DateType::class, [
                'required' => false,
                'label' => 'Date fin',
                'widget' => 'single_text',
            ])
            ->add('etat', ChoiceType::class, [
                'required' => false,
                'label' => 'État',
                'choices' => [
                    'En attente' => 'EN_ATTENTE',
                    'Validée' => 'VALIDEE',
                    'En préparation' => 'EN_PREPARATION',
                    'Prête' => 'PRETE',
                    'En livraison' => 'EN_LIVRAISON',
                    'Livrée' => 'LIVREE',
                    'Annulée' => 'ANNULEE',
                    'Terminée' => 'TERMINEE'
                ],
                'placeholder' => 'Tous les états'
            ])
            ->add('typeLivraison', ChoiceType::class, [
                'required' => false,
                'label' => 'Type de livraison',
                'choices' => [
                    'Sur place' => 'SUR_PLACE',
                    'À emporter' => 'A_EMPORTER',
                    'Livraison' => 'LIVRAISON'
                ],
                'placeholder' => 'Tous les types'
            ])
            ->add('filtrer', SubmitType::class, [
                'label' => 'Filtrer',
                'attr' => ['class' => 'btn btn-primary']
            ])
            ->add('reinitialiser', SubmitType::class, [
                'label' => 'Réinitialiser',
                'attr' => ['class' => 'btn btn-secondary'],
                'validation_groups' => false
            ]);
    }

    public function configureOptions(OptionsResolver $resolver): void
    {
        $resolver->setDefaults([
            'method' => 'GET',
            'csrf_protection' => false,
        ]);
    }
}